import React, { Component } from 'react';
import { observer, inject } from 'mobx-react';

@inject('store')
@observer
export default class LobbyPage extends Component {
  componentWillMount() {
    const { store } = this.props;

    this.interval = setInterval(() => store.refreshGames(), 5000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  start() {
    const { store, history } = this.props;
    store.start().then(game => {
      if (game) {
        history.push(`/play/${game.id}`);
      }
    });
  }

  join(game) {
    const { store, history } = this.props;
    store.join(game).then(game => {
      if (game) {
        history.push(`/play/${game.id}`);
      }
    });
  }

  render() {
    const { loading, games } = this.props.store;
    const ownedGame = games.find(({owned}) => owned);
    const publicGames = games.filter(({owned}) => !owned);

    return <div className="row game-page">
      <div className="col-sm-12">
        <h1>Choose a room</h1>
        {loading &&
        <div className="loader">
          <i className="fa fa-spinner fa-spin"></i>
        </div>
        }
        {ownedGame &&
        <div className="card">
          <div className="card-body">
            <p className="card-text">This is your own game</p>
            {!ownedGame.opponent &&
            <p>Waiting for opponent...</p>
            }
            {ownedGame.opponent &&
            <p className="card-text">Opponent: {ownedGame.opponent.name}</p>
            }
            {ownedGame.opponent &&
            <button className="btn btn-success" onClick={() => this.start()}>Play</button>
            }
          </div>
        </div>
        }

        <h2 className="mt-3">Battles around you...</h2>
        <div className="list-group">
        {publicGames.map(game =>
          <div key={game.id} className="list-group-item">
            <p>{game.owner.name}</p>
            <button className="btn btn-success" onClick={() => this.join(game)}>Join</button>
          </div>
        )}
        </div>
      </div>
    </div>
  }
}