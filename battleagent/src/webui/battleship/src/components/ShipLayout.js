import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { observer } from 'mobx-react';

import interact from 'interact.js';
import Gridboard from './Gridboard';

@observer
class ShipLayout extends Component {
  onShipDrop(e) {
    const { game } = this.props;
    const { shipID, x, y, orientation } = e;

    game.setLayout(shipID, x, y, orientation);
  }

  onStartGame() {
    const { game } = this.props;
    game.commitLayout();
  }

  componentDidMount() {
    interact('.ship-layout .draggable').draggable({});
  }

  render() {
    const { game } = this.props;
    const { size } = game;
    const ready = game.unboundShips.length === 0;

    return <div className="ship-layout" ref={node => this.element = node}>
      <div className="row">
        <div className="col-sm-7">
          <h3>Preparing your battle array</h3>
          <Gridboard mode="tracker"
                     size={size}
                     ships={game.ships}
                     onCellClick={console.log.bind(this)}
                     onCellDrop={(e) => this.onShipDrop(e)} />
        </div>
        <div className="col-sm-5">
          <h4>Ships</h4>
          <ul className="list-group">
            {game.ships.map(ship => (
            <li key={ship.shipID} className="list-group-item">
              <ShipItem name={ship.name}
                        shipID={ship.shipID}
                        hitPoints={ship.hitPoints}
                        orientation={ship.orientation} />
            </li>
            ))}
          </ul>
        </div>
      </div>
      {ready &&
      <div className="row mt-3">
        <div className="col-sm-7">
          <button className="btn btn-primary d-block mx-auto"
                  onClick={() => this.onStartGame()}>
            START
          </button>
        </div>
      </div>
      }
    </div>;
  }
}

const ShipItem = ({name, shipID, hitPoints, orientation}) => (
  <div className="ship-item">
    {(typeof orientation !== 'undefined') && <i className="fa fa-check"></i>}
    {name}
    <span className="float-right">
      {new Array(hitPoints).fill(0).map((_, i) => (
        <i key={i} className="fas fa-circle"></i>
      ))}
      <span className="ship-icon draggable" draggable data-ship-id={shipID} data-orientation="1">
        <i className="fa fa-ellipsis-v"></i>
      </span>
      <span className="ship-icon draggable" draggable data-ship-id={shipID} data-orientation="0" >
        <i className="fa fa-ellipsis-h"></i>
      </span>
    </span>
  </div>
);

export default ShipLayout;