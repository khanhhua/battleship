import React, { Component } from 'react';
import { observer, inject } from 'mobx-react';

import Gridboard from 'components/Gridboard';
import ShipLayout from "./components/ShipLayout";

@inject('store')
@observer
class GamePage extends Component {
  componentWillMount() {
    const gameId = this.props.match.params.id;

    this.props.store.load(gameId);
  }

  render() {
    const { activeGame } = this.props.store;
    if (!activeGame) {
      return <div className="game-page">
        <div className="loader">
          <i className="fa fa-spinner fa-spin"></i>
        </div>
      </div>
    }

    const { size, status, activities } = activeGame;

    return <div className="game-page">
      <div className="row">
        <div className="col-sm-4">
          <h4>Status</h4>
          <ul className="list-group list-group-flush">
            <li className="list-group-item">Status: {statusText(status)}</li>
            {activities.map(activity => (
            <li className="list-group-item">{activity.text}</li>
            ))}
          </ul>

        </div>

        {status === 1 &&
        <div className="col-sm-8">
          <ShipLayout game={activeGame} />
        </div>
        }

        {status === 2 &&
        <div className="col-sm-5">
          <h3>Tracker</h3>
          <Gridboard mode="tracker" size={size}/>
        </div>
        }
        {status === 2 &&
        <div className="col-sm-3">
          <h4>Scoreboard</h4>
          <ul className="list-group">
            <li className="list-group-item">{activeGame.owner.name}: 0</li>
            {activeGame.opponent &&
            <li className="list-group-item">{activeGame.opponent.name}: 0</li>
            }
          </ul>
        </div>
        }
      </div>

      {status === 2 &&
      <div className="row mt-3">
        <div className="col-sm-7 offset-sm-1">
          <h2>Primary table</h2>
          <Gridboard mode="primary" size={size}/>
        </div>
        <div className="col-sm-4">
          <h4>Ships</h4>
          <ul className="list-group">
            <li className="list-group-item">Destroyer</li>
            <li className="list-group-item">Destroyer</li>
            <li className="list-group-item">Submarine</li>
            <li className="list-group-item">Cruiser</li>
            <li className="list-group-item">Cruiser</li>
            <li className="list-group-item">Battleship</li>
            <li className="list-group-item">Carrier</li>
          </ul>
        </div>
      </div>
      }
    </div>
  }
}

function statusText(status) {
  switch (status) {
    case 1: return 'Pending (awaiting opponent...)';
    case 2: return 'Ready (preping battle array...)';
    case 3: return 'Fire in the hole';
    case 4: return 'Game over';
    default: return 'Error';
  }
}

export default GamePage;