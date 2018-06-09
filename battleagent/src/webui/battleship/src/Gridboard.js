import React, {Component} from "react";
import 'Gridbard.scss';

export default ({mode, size, onCellClick}) => {
  const rowLabels = new Array(size).fill(0).map((_, index) => String.fromCharCode(65 + index));
  const colLabels = new Array(size).fill(0).map((_, index) => index + 1);

  return (
    <div className={`gridboard ${mode==='tracker' && 'tracker'}`}>
      <div className="gridboard-row">
        <span className="gridboard-header-cell"></span>
        {colLabels.map(colLabel => (
        <span key={colLabel} className="gridboard-header-cell">{colLabel}</span>
        ))}
      </div>
      {rowLabels.map(rowLabel => (
      <div key={rowLabel} className="gridboard-row">
        <span className="gridboard-header-cell">{rowLabel}</span>
        {colLabels.map(colLabel => (
        <span key={colLabel} className="gridboard-cell">&nbsp;</span>
        ))}
      </div>
      ))}
    </div>
  )
}