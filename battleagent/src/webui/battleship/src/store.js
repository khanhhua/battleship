import { observable, computed, action } from 'mobx';

export default class Store {
  @observable loading = false;

  @observable player = new User();
  @observable games = [];

  @observable activeGame;

  @action refreshGames() {
    this.loading = true;
    fetch('/api/games').then(res => res.json()).then((games) => {
      this.loading = false;
      this.games = games;
    }).catch(e => {
      this.loading = false;
    });
  }

  @action start() {
    this.loading = true;
    return fetch('/api/rooms/start',
      {
        method: 'POST'
      }).then(res => res.json()).then(payload => {
        this.loading = false;

        const game = new Game(payload);
        this.activeGame = game;

        return game;
      });
  }

  @action join(game) {
    this.loading = true;
    return fetch(`/api/games/${game.id}/join`,
      {
        method: 'POST'
      }).then(res => res.json()).then(payload => {
      this.loading = false;
      const game = new Game(payload);
      this.activeGame = game;

      return game;
    });
  }

  @action load(gameID) {
    this.loading = true;
    return fetch(`/api/games/${gameID}`).then(res => res.json()).then(payload => {
      this.loading = false;
      this.activeGame = new Game(payload);
      this.activeGame.listen();

      return true;
    });
  }
}

export class User {
  @observable loading = false;

  @observable username = '';
  @observable password = '';

  @computed get isLoggedIn() {
    return !!this.username;
  }

  @action login() {
    const {username, password} = this;
    this.loading = true;

    return fetch(
      '/api/rooms',
        {
          body: JSON.stringify({username, password}),
          method: 'POST',
          headers: {
            'content-type': 'application/json'
          },
        }
      )
      .then(response => response.json())
      .then(payload => {
        this.loading = false;

        return payload === 'ok'
      })
  }
}

export class Game {
  static STATUS_PENDING = 1;
  static STATUS_READY = 2;
  static STATUS_PLAYING = 3;
  static STATUS_COMPLETE = 4;

  @observable loading = false;
  @observable activities = [];

  @observable ships = [
    {
      name: 'Destroyer',
      shipID: '0',
      hitPoints: 2,
      x: undefined,
      y: undefined,
      orientation: undefined
    },
    {
      name: 'Destroyer',
      shipID: '1',
      hitPoints: 2,
      x: undefined,
      y: undefined,
      orientation: undefined
    },
    {
      name: 'Submarine',
      shipID: '2',
      hitPoints: 3,
      x: undefined,
      y: undefined,
      orientation: undefined
    }
    ,
    {
      name: 'Cruiser',
      shipID: '3',
      hitPoints: 3,
      x: undefined,
      y: undefined,
      orientation: undefined
    }
    ,
    {
      name: 'Cruiser',
      shipID: '4',
      hitPoints: 3,
      x: undefined,
      y: undefined,
      orientation: undefined
    },
    {
      name: 'Battleship',
      shipID: '5',
      hitPoints: 4,
      x: undefined,
      y: undefined,
      orientation: undefined
    },
    {
      name: 'Carrier',
      shipID: '6',
      hitPoints: 5,
      x: undefined,
      y: undefined,
      orientation: undefined
    }
  ];

  id = null;
  owned = false;

  size = 0;
  owner = null;
  @observable opponent = null;

  @observable status= 0;

  constructor({id, owned, size, status, owner, opponent}) {
    this.id = id;
    this.owned = owned;
    this.size = size;
    this.status = status;
    this.owner = owner;
    this.opponent = opponent;
  }

  listen() {
    const sse = new EventSource(`/sse/games/${this.id}`);

    sse.addEventListener('message', (e) => {
      const data = JSON.parse(e.data);
      switch (data.type) {
        case 'status': this.status = data.status; break;
        case 'opponent': this.opponent = data.opponent; break;
        case 'ships': this.ships = data.ships; break;
      }
    });

    this.sse = sse;
  }

  @action setLayout(shipID, x, y, orientation) {
    const index = this.ships.findIndex(({shipID:id}) => id == shipID);
    if (index === -1) {
      return;
    }
    const ship = Object.assign({}, this.ships.find(({shipID:id}) => id == shipID), {x, y, orientation});
    this.ships.splice(index, 1, ship);
  }

  @action commitLayout() {
    const positions = this.ships.map(it => ({ x: it.x, y: it.y, orientation: it.orientation }));

    return fetch(`/api/games/${this.id}/layout`, {
      method: 'POST',
      body: JSON.stringify(positions)
    }).then(res => res.json()).then(payload => {
      if (payload === 'ok') {
        this.status = Game.STATUS_READY;
      }
    });
  }

  @computed get unboundShips() {
    return this.ships.filter(it => typeof it.orientation === 'undefined');
  }
}