package com.khanhhua.battleship.battleagent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.*;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameServiceTest {
  @Autowired
  private GameService gameService;

  @Test
  public void discoverGames() throws IOException, InterruptedException {
    gameService.discoverGames();

    InetAddress bcastAddress = InetAddress.getByName("255.255.255.255");

    DatagramSocket socket = new DatagramSocket();
    socket.setBroadcast(true);
    byte[] buffer = "1234567890".getBytes();

    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, bcastAddress, GameService.BROADCAST_PORT);
    socket.send(packet);
    sleep(1000L);

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