import {configure, observable, computed, action } from 'mobx';

export default class Store {
  @observable player = new User();
}

export class User {
  @observable username = '';
  @observable password = '';

  @computed get isLoggedIn() {
    return !!this.username;
  }

  @action login() {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve(true);
      }, 3000);
    });
  }
}