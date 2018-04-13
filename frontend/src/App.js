import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import store from './store/store';
import Root from './components/Root';
// import 'react-dates/initialize';
import "./style/style.scss";
// import 'react-dates/lib/css/_datepicker.css';
// import 'font-awesome/css/font-awesome.min.css';

ReactDOM.render(
  <Root store={store} />,
  document.getElementById('root')
);
