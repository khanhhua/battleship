package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Game;
import com.khanhhua.battleship.commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/rooms")
public class RoomsController {
    @Autowired
    private GameService gameService;

    @PostMapping(value = "", produces = "application/json")
    public String createRoom(@RequestBody Player player) {
        gameService.login(player.getName());
        gameService.createGame();
        gameService.advertize(5);

        return "\"ok\"";
    }

    @GetMapping(value = "", produces = "application/json")
    public Game[] refreshRooms() {
        gameService.discoverGames();
        ArrayList<Game> games = new ArrayList<Game>();
        for (Map.Entry<Long, String> entry:gameService.getRemoteURLs()) {
          games.add(new Game(entry.getKey(), entry.getValue()));
        }

        Game[] output = new Game[games.size()];

        return games.toArray(output);
    }

    @PostMapping(value = "/start", produces = "application/json")
    public String startGame() {
        if (gameService.startGame()) {
            return "\"ok\"";
        }

        return "\"error\"";
    }
}
