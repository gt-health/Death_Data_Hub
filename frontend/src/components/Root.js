import React from 'react';
import { Provider } from 'react-redux';
import { Router, Route } from 'react-router-dom';//used to be BrowserRouter as Router
import StiApp from '../containers/sti_container.js';
import history from '../helpers/history';

const Root = ({ store }) => (
  <Provider store={store}>
    <Router history={history}>
      <Route path='/' component={StiApp} />
    </Router>
  </Provider>
);

export default Root;
