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

        return "\"ok\"";
    }

    @PostMapping(value = "/refresh", produces = "application/json")
    public String refreshRooms() {
        gameService.discoverGames();

        return "\"ok\"";
    }


}
