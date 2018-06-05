package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Game;
import com.khanhhua.battleship.commons.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Collections;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomsControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameService gameService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Before
    public void setUp() throws Exception {
    }

    @Test()
    public void createRoom() {
        Player player = new Player();
        player.setName("Tom");

        HttpHeaders headers = new HttpHeaders();
        headers.put("Accept", Collections.singletonList("application/json"));

        HttpEntity<Player> request = new HttpEntity<Player>(player, headers);
        String actual = this.restTemplate.postForObject("/api/rooms", request, String.class);

        assertThat(actual).isEqualTo("\"ok\"");
        assertThat(gameService.getLocalGame()).isNotNull();
    }

    @Test
    public void refreshRooms() throws InterruptedException {
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

        HttpHeaders headers = new HttpHeaders();
        headers.put("Accept", Collections.singletonList("application/json"));

        Game[] actual = this.restTemplate.getForObject("/api/rooms", Game[].class);

        assertThat(actual[0].getId()).isEqualTo(1234567890L);
    }

    @Test
    public void startGame() {
    }
}