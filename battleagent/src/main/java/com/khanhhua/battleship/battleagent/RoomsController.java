package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/refresh", produces = "application/json")
    public String refreshRooms() {
        gameService.discoverGames();

        return "\"ok\"";
    }

    @PostMapping(value = "/start", produces = "application/json")
    public String startGame() {
        if (gameService.startGame()) {
            return "\"ok\"";
        }

        return "\"error\"";
    }
}
