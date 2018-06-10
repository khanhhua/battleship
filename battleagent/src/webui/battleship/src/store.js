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
  status = 0;
  owner = null;
  opponent = null

  constructor({id, owned, size, status, owner, opponent}) {
    this.id = id;
    this.owned = owned;
    this.size = size;
    this.status = status;
    this.owner = owner;
    this.opponent = opponent;
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
    const positions = this.ships.map(({ x, y, orientation }) => { x, y, orientation });

    return fetch(`/api/games/${this.id}/layout`, {
      method: 'POST',
      body: JSON.stringify(positions)
    }).then(res => res.json()).then(payload => {
      return payload === 'ok';
    });
  }

  @computed get unboundShips() {
    return this.ships.filter(it => typeof it.orientation === 'undefined');
  }
}