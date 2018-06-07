import React, { Component } from 'react';

export default class LoginPage extends Component {
  login() {
    console.log('Logging in...');
  }

  render() {
    return <div className="row login-page">
      <div className="col-sm-6 offset-md-3">
        <h1>Login Page</h1>
        <div className="form-horizontal">
          <div className="row">
            <label htmlFor="username" className="col-sm-3">Username</label>
            <div className="col-sm-9">
              <input id="username" className="form-control" />
            </div>
          </div>
          <div className="row" style={{paddingTop: '1rem'}}>
            <button className="btn btn-primary mx-auto" onClick={()=>this.login()}>Login</button>
          </div>
        </div>
      </div>
    </div>
  }
}