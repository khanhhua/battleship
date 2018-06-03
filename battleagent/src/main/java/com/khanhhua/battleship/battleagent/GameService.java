package com.khanhhua.battleship.battleagent;

import com.khanhhua.battleship.commons.Game;
import com.khanhhua.battleship.commons.Player;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class GameService {
    final int DEFAULT_GAME_SIZE = 10;

    private Game localGame;
    private Player player;
    private HashMap<Long, String> remoteURLs = new HashMap<Long, String>();

    public Game getLocalGame() {
        return localGame;
    }

    public void login(String name) {
        this.player = new Player();
        this.player.setName(name);
    }

    public Game createGame() {
        localGame = new Game(player, DEFAULT_GAME_SIZE);

        return localGame;
    }

    public boolean startGame() {
        int status = localGame.getStatus();
        try {
            localGame.setStatus(Game.STATUS_PLAYING);

            String url = "http://localhost:9191/api/remote/games/" + localGame.getId();
            RestTemplate rest = new RestTemplate();
            HashMap<String, Integer> values = new HashMap<String, Integer>();
            values.put("status", Game.STATUS_PLAYING);
            rest.put(url, values);

            return true;
        } catch (RestClientException ex) {
            localGame.setStatus(status);

            return false;
        }
    }

    public Game joinGame(int gameID) {
        String url = remoteURLs.get(gameID);
        RestTemplate template;

        try {
            template = new RestTemplate();
            String result = template.postForObject(url + "/players", null, String.class);

            if (result == "ok") {
                template = new RestTemplate();
                Game game = template.getForObject(url, Game.class);

                return game;
            } else {
                System.err.println("Could not join localGame. Reason: " + result);
                return null;
            }
        } catch (RestClientException ex) {
            System.err.println("Could not join localGame. Reason: " + ex.getMessage());
            return null;
        }
    }

    public boolean verifyHit(long gameID, int x, int y) {
        String url = remoteURLs.get(gameID);
        RestTemplate template;

        try {
            template = new RestTemplate();
            HashMap<String, Integer> vars = new HashMap<String, Integer>();
            vars.put("x", x);
            vars.put("y", y);
            String result = template.postForObject(url + "/verify-hit", vars, String.class);

            if (result == "ok") {
                return true;
            } else {
                return false;
            }
        } catch (RestClientException ex) {
            System.err.println("Could not verify hit. Reason: " + ex.getMessage());
            return false;
        }
    }

    public Game findByID(long id) {
        if (id == this.localGame.getId()) {
            return localGame;
        } else if (remoteURLs.containsKey(id)) {
            String url = remoteURLs.get(id);

            try {
                RestTemplate template = new RestTemplate();
                return template.getForObject(url, Game.class);
            } catch (RestClientException ex) {
                System.err.printf("Could not find localGame %d. Reason: %s\n", id, ex.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    public void discoverGames() {
        remoteURLs.put(1L, "http://localhost:9191/api/remote/games/1");
    }

}
