import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import interact from 'interact.js';
import './Gridbard.scss';

export default class Gridboard extends Component {

  componentDidMount() {
    const { onCellDrop } = this.props;

    interact('.gridboard-cell').dropzone({
      overlap: 'pointer',
      ondrop (event) {
        const { column, row } = event.target.dataset;
        const { shipId, orientation } = event.relatedTarget.dataset;
        console.log(`Ship #${shipId} was dropped into %o`, { x:column, y:row, orientation });

        onCellDrop({ shipID: shipId, x:column, y:row, orientation });
      },
      ondropactivate (event) {
        // add active dropzone feedback
        // event.target.classList.add('drop-active');
      },
      ondragenter (event) {
        event.target.classList.add('drop-active');
      },
      ondragleave (event) {
        // remove active dropzone feedback
        event.target.classList.remove('drop-active');
      },
      ondropdeactivate (event) {
        event.target.classList.remove('drop-active');
      }
    });
  }

  render() {
    const {mode, size, onCellClick, ships} = this.props;
    const boundShips = ships.filter(it => typeof it.orientation !== 'undefined');
    const stepSize = mode === 'tracker' ? 32 : 48;

    const rowLabels = new Array(size).fill(0).map((_, index) => String.fromCharCode(65 + index));
    const colLabels = new Array(size).fill(0).map((_, index) => index + 1);

    return (
      <div className={`gridboard ${mode==='tracker' && 'tracker'}`}
           ref={node => this.element = node}>
        <div className="gridboard-row">
          <span className="gridboard-header-cell">&nbsp;</span>
          {colLabels.map(colLabel => (
            <span key={colLabel} className="gridboard-header-cell">{colLabel}</span>
          ))}
        </div>
        {rowLabels.map((rowLabel, row) => (
          <div key={rowLabel} className="gridboard-row">
            <span className="gridboard-header-cell">{rowLabel}</span>
            {colLabels.map((colLabel, col) => (
              <span key={colLabel} className="gridboard-cell"
                    data-column={col}
                    data-row={row}
                    onClick={(e) => onCellClick(e)}>&nbsp;</span>
            ))}
          </div>
        ))}

        {!!(boundShips && boundShips.length) &&
        <div className="gridboard-ships">
          {boundShips.map(ship => {
            const orientation = ship.orientation == 0 ? 'horizontal' : 'vertical';
            const x = `${ship.x * stepSize}px`;
            const y = `${ship.y * stepSize}px`;
            const cx = `gridboard-ship ${orientation} hits-${ship.hitPoints}`;

            return <div key={ship.shipID} className={cx} style={{transform: `translate(${x}, ${y})`}}></div>;
          })}
        </div>
        }
      </div>
    );
  }
}
