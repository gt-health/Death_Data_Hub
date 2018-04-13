import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import Search from '../containers/search_container.js';
import CaseView from '../containers/case_view_container.js';
// import TestComp from './TestComp';
import CaseEdit from '../containers/case_edit_container.js';
import Cases from '../containers/cases_container.js';

export default class StiApp extends Component {

  constructor(props) {
    super(props);
  }

  componentDidMount() {
  }

  render() {
    return (
      <div id="sti">
        {/*<Route path="/" component={Navbar}/>*/}
        <Switch>
          <Route exact path="/" component={Search}/>
          <Route path="/cases/:caseId/view" component={CaseView}/>
          {/*<Route path="/cases/:caseId/view" component={TestComp}/>*/}
          <Route path="/cases/:caseId/edit" component={CaseEdit}/>
          <Route path="/cases" component={Cases}/>
        </Switch>
      </div>
    )
  }
}
