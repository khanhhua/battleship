package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameServiceTest {
  @Autowired
  private GameService gameService;

  @Autowired
  private ThreadPoolTaskExecutor taskExecutor;

  @Test
  public void createGame() {
    gameService.login("Tom");
    Game game = gameService.createGame();
    assertThat(game.getOwner()).isNotNull();
    assertThat(game.getOwner().getName()).isEqualTo("Tom");
  }

  @Test
  public void discoverGames() {
    taskExecutor.execute(new Runnable() {
      public void run() {
        try {
          InetAddress bcastAddress = InetAddress.getByName("255.255.255.255");

          DatagramSocket socket = new DatagramSocket();
          socket.setBroadcast(true);
          byte[] buffer = "1234567890".getBytes();

          DatagramPacket packet = new DatagramPacket(buffer, buffer.length, bcastAddress, GameService.BROADCAST_PORT);
          socket.send(packet);
        } catch (IOException ex) {
          System.err.println(ex.getMessage());
        }
      }
    });

    gameService.discoverGames();
    assertThat(gameService.getRemoteURLs().size()).isEqualTo(1);
  }

  @Test
  public void advertize() throws IOException {
    gameService.createGame();
    gameService.advertize(1);

    DatagramSocket socket = new DatagramSocket(GameService.BROADCAST_PORT);

    byte[] buffer = new byte[64];
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    socket.receive(packet);

    long gameID = Long.parseLong(new String(packet.getData()).trim());
    assertThat(gameID).isEqualTo(gameService.getLocalGame().getId());

    socket.close();
  }
}