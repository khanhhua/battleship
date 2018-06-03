package com.khanhhua.battleship.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.json.*;
import org.springframework.test.context.junit4.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = TestConfiguration.class)
@RunWith(SpringRunner.class)
@JsonTest
public class PlayerTest {
    private JacksonTester<Player> json;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testSerialize() throws Exception {
        Player player = new Player();
        player.setName("Tom");

        String actual = json.write(player).getJson();
        assertThat(actual).isEqualTo("{\"name\":\"Tom\"}");
    }
}