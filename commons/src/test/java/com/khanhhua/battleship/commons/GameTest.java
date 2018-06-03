package com.khanhhua.battleship.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = TestConfiguration.class)
@RunWith(SpringRunner.class)
@JsonTest
public class GameTest {
    private JacksonTester<Game> json;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testSerialize() throws Exception {
        Player owner = new Player();
        owner.setName("Tom");
        Game game = new Game(owner, 3);
        game.setId(1528046810344L);
        JsonContent<Game> actual = json.write(game);
        assertThat(actual).isEqualToJson("game.json");
    }
}