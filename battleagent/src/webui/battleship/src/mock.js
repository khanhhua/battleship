import Pretender from 'fetch-pretender';
import _ from 'lodash';

const initialShips = [
  {
    "name": "Destroyer",
    "x": null,
    "y": null,
    "orientation": null
  },
  {
    "name": "Destroyer",
    "x": null,
    "y": null,
    "orientation": null
  },
  {
    "name": "Submarine",
    "x": null,
    "y": null,
    "orientation": null
  },
  {
    "name": "Cruiser",
    "x": null,
    "y": null,
    "orientation": null
  },
  {
    "name": "Cruiser",
    "x": null,
    "y": null,
    "orientation": null
  },
  {
    "name": "Battleship",
    "x": null,
    "y": null,
    "orientation": null
  },
  {
    "name": "Carrier",
    "x": null,
    "y": null,
    "orientation": null
  }
];

const games = [
  {
    "id": 1528046810344,
    "owned": true,
    "size": 10,
    "status": 1,
    "owner": {
      "name": "Tom"
    },
    "opponent": null
  },
  {
    "id": 1528092000001,
    "owned": false,
    "size": 10,
    "status": 1,
    "owner": {
      "name": "Jerry"
    },
    "opponent": null
  },
  {
    "id": 1528092000002,
    "owned": false,
    "size": 10,
    "status": 1,
    "owner": {
      "name": "Bane"
    },
    "opponent": null
  }
];

function success(body) {
    return [200, {'Content-Type': 'application/json'}, JSON.stringify(body)];
}

/**
 * Create new room under the name of the current player (aka. user) with a password
 *
 * @param req
 * @return {*}
 */
function createRoom(req) {
    return success('ok');
}

function getGames(req) {
    return success(games);
}

function getGame(req) {
  return success(games.find(({id}) => parseInt(req.params.id, 10) === id));
}

function setLayout(req) {
  const game = games.find(({id}) => parseInt(req.params.id, 10) === id);
  const positions = JSON.parse(req.requestBody);

  game.ships = _.assign([], _.zip(initialShips, positions).map(([i, p]) => _.assign(i, p)));
  return success('ok');
}

function startGame() {
  return success(games.find(it => it.owned === true));
}

function joinGame(req) {
  return success(games.find(({id}) => parseInt(req.params.id, 10) === id));
}

const server = new Pretender();
server.post('/api/rooms', createRoom, 2000);
server.get('/api/games', getGames, 2000);
server.get('/api/games/:id', getGame, 2000);
server.post('/api/games/:id/layout', setLayout, 2000);
server.post('/api/games/start', startGame, 2000);
server.post('/api/games/:id/join', joinGame, 2000);

export default server;