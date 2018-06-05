package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomsControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameService gameService;

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
    public void refreshRooms() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Accept", Collections.singletonList("application/json"));

        HttpEntity<Void> request = new HttpEntity<Void>(headers);
        String actual = this.restTemplate.postForObject("/api/rooms/refresh", request, String.class);

        assertThat(actual).isEqualTo("\"ok\"");
    }

    @Test
    public void startGame() {
    }
}