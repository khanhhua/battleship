package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Game;
import com.khanhhua.battleship.commons.ShipPosition;
import com.khanhhua.battleship.commons.Shot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games")
public class GamesController {
    @Autowired
    private GameService gameService;

    @PostMapping(value = "/{id}/layout", produces = "application/json")
    public String setLayout(@PathVariable("id") long gameID, @RequestBody ShipPosition[] positions) {
        Game game = gameService.findByID(gameID);

        for (int shipID = 0; shipID < positions.length; shipID++) {
            ShipPosition position = positions[shipID];

            game.putShipAt(position.getX(), position.getY(), shipID, position.getOrientation());
        }

        return "\"ok\"";
    }

    @PostMapping(value = "/{id}/shots", produces = "application/json")
    public String makeShot(@PathVariable("id") long gameID, @RequestBody Shot shot) {
        int x = shot.getX();
        int y = shot.getY();

        Game game = gameService.findByID(gameID);
        if (gameService.verifyHit(gameID, x, y)) {
            game.mark(x, y, true);
        } else {
            game.mark(x, y, false);
        }

        return "\"ok\"";
    }
}
