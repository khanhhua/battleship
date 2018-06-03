package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Game;
import com.khanhhua.battleship.commons.Player;
import com.khanhhua.battleship.commons.Shot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("api/remote")
public class RemoteController {
    @Autowired
    private GameService gameService;

    /**
     * Accepts a start game request from the owner of the game
     *
     * @param gameID
     * @param updates
     * @return
     * @throws IllegalAccessException
     */
    @PutMapping(value = "/games/{id}", produces = "application/json")
    public String updateGame(@PathVariable("id") int gameID, @RequestBody HashMap<String, Object> updates) throws IllegalAccessException {
        Game game = gameService.findByID(gameID);
        if (game.isOwned()) {
            throw new IllegalAccessException("Permission denied");
        }
        // TODO Security - Consistency: Verify the request to start game comes from the owner and the owner only

        if (updates.containsKey("status")) {
            Integer status = (Integer)updates.get("status");
            game.setStatus(status);
        }

        return "\"ok\"";
    }

    /**
     * Another agent may join this agent
     * @param player
     * @return
     */
    @PostMapping(value = "/games/{id}/players", produces = "application/json")
    public String joinGame(@PathVariable("id") int gameID, @RequestBody Player player) {
        Game game = gameService.findByID(gameID);
        game.setOppoent(player);

        return "\"ok\"";
    }

    /**
     * Accepts a verify-hit request from the other player.
     * Game state is not shared - remote machine should not have any known of how I layout my ships.
     *
     * @param gameID
     * @param shot
     * @return
     */
    @PostMapping(value = "/games/{id}/verify-hit", produces = "application/json")
    public String verifyHit(@PathVariable int gameID, @RequestBody Shot shot) {
        return "\"ok\"";
    }

    /**
     * Game discovery handler
     *
     * @return
     */
    @GetMapping(value = "/games")
    public Game[] getGames() {
        if (gameService.getLocalGame() == null) {
            return new Game[]{};
        }

        return new Game[]{gameService.getLocalGame()};
    }
}
