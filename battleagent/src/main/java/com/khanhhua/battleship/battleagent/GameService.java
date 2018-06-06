package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Game;
import com.khanhhua.battleship.commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.*;

@Service
public class GameService {
  public static final int SERVICE_PORT = 9191;
  public static final int BROADCAST_PORT = 9190;

  final int DEFAULT_GAME_SIZE = 10;

  private Game localGame;
  private Player player;
  private HashMap<Long, String> remoteURLs = new HashMap<Long, String>();

  @Autowired
  private ThreadPoolTaskExecutor taskExecutor;
  private Runnable broadcaster;
  private Runnable receiver;

  public Game getLocalGame() {
    return localGame;
  }

  public Set<Map.Entry<Long, String>> getRemoteURLs() {
    return remoteURLs.entrySet();
  }

  public void login(String name) {
    this.player = new Player();
    this.player.setName(name);
  }

  public Game createGame() {
    localGame = new Game(player, DEFAULT_GAME_SIZE);

    return localGame;
  }

  public boolean startGame() {
    int status = localGame.getStatus();
    try {
      localGame.setStatus(Game.STATUS_PLAYING);

      String url = String.format("http://0.0.0.0:%s/api/remote/games/%d", SERVICE_PORT, localGame.getId());
      RestTemplate rest = new RestTemplate();
      HashMap<String, Integer> values = new HashMap<String, Integer>();
      values.put("status", Game.STATUS_PLAYING);
      rest.put(url, values);

      return true;
    } catch (RestClientException ex) {
      localGame.setStatus(status);

      return false;
    }
  }

  public Game joinGame(int gameID) {
    String url = remoteURLs.get(gameID);
    RestTemplate template;

    try {
      template = new RestTemplate();
      String result = template.postForObject(url + "/players", null, String.class);

      if (result == "ok") {
        template = new RestTemplate();
        Game game = template.getForObject(url, Game.class);

        return game;
      } else {
        System.err.println("Could not join localGame. Reason: " + result);
        return null;
      }
    } catch (RestClientException ex) {
      System.err.println("Could not join localGame. Reason: " + ex.getMessage());
      return null;
    }
  }

  public boolean verifyHit(long gameID, int x, int y) {
    String url = remoteURLs.get(gameID);
    RestTemplate template;

    try {
      template = new RestTemplate();
      HashMap<String, Integer> vars = new HashMap<String, Integer>();
      vars.put("x", x);
      vars.put("y", y);
      String result = template.postForObject(url + "/verify-hit", vars, String.class);

      if (result == "ok") {
        return true;
      } else {
        return false;
      }
    } catch (RestClientException ex) {
      System.err.println("Could not verify hit. Reason: " + ex.getMessage());
      return false;
    }
  }

  public Game findByID(long id) {
    if (id == this.localGame.getId()) {
      return localGame;
    } else if (remoteURLs.containsKey(id)) {
      String url = remoteURLs.get(id);

      try {
        RestTemplate template = new RestTemplate();
        return template.getForObject(url, Game.class);
      } catch (RestClientException ex) {
        System.err.printf("Could not find localGame %d. Reason: %s\n", id, ex.getMessage());
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Discover games available within the current LAN by listening
   */
  public synchronized void discoverGames() {
    if (receiver != null) {
      return;
    }
    final String localAddress;

    try {
      localAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return;
    }

    receiver = new Runnable() {
      public void run() {
        try {
          DatagramSocket socket = new DatagramSocket(BROADCAST_PORT);
          long now = System.currentTimeMillis();
          long timeout = 5000L;

          while (timeout > 0) {
            byte[] buffer = new byte[64];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            long gameID = Long.parseLong(new String(packet.getData()).trim());

            System.out.printf("Receiving game [%d] at %s...\n", gameID, packet.getAddress().getHostAddress());
            if (!"test".equalsIgnoreCase(System.getenv("JAVA_ENV")) &&
              localAddress.equalsIgnoreCase(packet.getAddress().getHostAddress())) {
              return;
            }

            String url = String.format("http://%s:%s/api/remote/games/%d",
              packet.getAddress().getHostAddress(), SERVICE_PORT, gameID);

            System.out.printf("Found %d at %s\n", gameID, url);
            remoteURLs.put(gameID, url);

            long delta = System.currentTimeMillis() - now;
            timeout -= delta;
            now += delta;
          }

          GameService.this.receiver = null;
          System.out.printf("Remote games count %d\n", remoteURLs.size());
        } catch (SocketException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };

    try {
      final Future future = this.taskExecutor.submit(receiver);
      future.get(5L, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      System.err.println(ex.getMessage());
    } catch (ExecutionException ex) {
      System.err.println(ex.getMessage());
    } catch (TimeoutException ex) {
    }
  }

  /**
   * Broadcast the availability of local game within the local network 255.255.255.255
   *
   * @param minutes
   */
  public synchronized void advertize(final int minutes) {
    if (localGame == null) {
      return;
    }

    if (this.broadcaster != null) {
      return;
    }

    broadcaster = new Runnable() {
      public void run() {
        try {
          String gameID = String.valueOf(localGame.getId());
          InetAddress bcastAddress = InetAddress.getByName("255.255.255.255");

          DatagramSocket socket = new DatagramSocket();
          socket.setBroadcast(true);
          byte[] buffer = gameID.getBytes();

          for (int i = 0; i < minutes * 60; i++) {
            System.out.printf("Advertizing game [%d]...\n", localGame.getId());
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, bcastAddress, BROADCAST_PORT);
            socket.send(packet);

            sleep(1000L);
          }

          socket.close();
          socket = null;
          GameService.this.broadcaster = null;
        } catch (UnknownHostException ex) {
          System.err.println(ex.getMessage());
        } catch (SocketException ex) {
          System.err.println(ex.getMessage());
        } catch (IOException ex) {
          System.err.println(ex.getMessage());
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }
      }
    };

    this.taskExecutor.execute(this.broadcaster);
  }
}
