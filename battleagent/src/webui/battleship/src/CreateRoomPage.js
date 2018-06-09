import React, { Component } from 'react';
import { observer, inject } from 'mobx-react';

@inject('store')
@observer
class CreateRoomPage extends Component {
  login() {
    console.log('Logging in...');
    const { history } = this.props;
    const { player } = this.props.store;

    player.login().then(loggedIn => {
      if (loggedIn) {
        history.push('/lobby')
      }
    });
  }

  update = (key) => ({target: {value}}) => {
    const { player } = this.props.store;
    player[key] = value;
  }

  render() {
    const { player } = this.props.store;
    const { loading } = player;

    return <div className="row login-page">
      <div className="col-sm-4 offset-sm-4">
        <h1>Create Your Room</h1>
        {loading &&
        <div className="loader">
          <i className="fa fa-spinner fa-spin"></i>
        </div>
        }
        <div className="form-horizontal">
          <div className="row">
            <label htmlFor="username" className="col-sm-3">Username</label>
            <div className="col-sm-9">
              <input id="username" className="form-control"
                     disabled={loading}
                     value={player.username}
                     onChange={this.update('username')} />
            </div>
          </div>

          <div className="row">
            <label htmlFor="password" className="col-sm-3">Password</label>
            <div className="col-sm-9">
              <input id="password" type="password" className="form-control"
                     disabled={loading}
                     value={player.password}
                     onChange={this.update('password')} />
            </div>
          </div>

          <div className="row" style={{paddingTop: '1rem'}}>
            <button className="btn btn-primary mx-auto"
                    disabled={loading}
                    onClick={() => this.login()}>Login</button>
          </div>
        </div>
      </div>
    </div>
  }
}

export default CreateRoomPage;