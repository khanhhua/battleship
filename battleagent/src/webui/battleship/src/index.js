import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'mobx-react';
import './index.css';
import App from './App';
import Store from './store';
// import registerServiceWorker from './registerServiceWorker';

const store = new Store();

const Main = () => (
  <Provider store={store}>
    <App />
  </Provider>
);

ReactDOM.render(<Main />, document.getElementById('root'));
// registerServiceWorker();
