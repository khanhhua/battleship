import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import './App.css';
import CreateRoomPage from './CreateRoomPage';
import LobbyPage from './LobbyPage';
import GamePage from './GamePage';

class App extends Component {
  render() {
    return (
      <div className="App container">
        <header className="App-header">
          <h1 className="App-title">Battleship</h1>
        </header>

        <Router>
          <div>
            <Route exact path="/" component={CreateRoomPage} />
            <Route exact path="/lobby" component={LobbyPage} />
            <Route exact path="/play/:id" component={GamePage} />
          </div>
        </Router>
      </div>
    );
  }
}

export default App;
