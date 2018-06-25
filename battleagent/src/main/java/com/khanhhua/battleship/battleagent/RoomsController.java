package com.khanhhua.battleship.battleagent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/rooms")
public class RoomsController {
  @Autowired
  private GameService gameService;

  @PostMapping(value = "", produces = "application/json")
  public String createRoom(@RequestBody CredentialModel credentialModel) {
    // TODO Authenticate player
    gameService.login(credentialModel.getUsername());
    gameService.createGame();
    System.out.printf("createRoom() :: Local game: %s\n", gameService.getLocalGame().getOwner().getName());

    gameService.advertize(5);

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
