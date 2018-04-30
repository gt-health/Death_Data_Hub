import React from 'react';
import ReactDOM from 'react-dom';
import CaseSection from '../containers/case_section_container.js';
import { Route, Switch, Link } from 'react-router-dom';
import Form from "react-jsonschema-form";
import schema from '../helpers/schema.json';

export default class Case extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
    this.submitForm = this.submitForm.bind(this);
  }

  componentDidMount() {
    this.props.onGetCase(this.props.match.params.caseId);
  }

  componentWillMount() {}

  componentWillUnmount () {}

  componentDidUpdate(prevProps,prevState) {}

  submitForm() {
  	this.form.getWrappedInstance().submitButton.click();
  }

  render() {
    console.log('*** rendering case edit component ***');
    if (this.props.case.inProgress) {
      return (
        <div className="loading-wrapper">
          <div className="loading-message">
            <span className="loading-message-text">Loading ECR</span><span className="loading-message-icon"><i className="fa fa-circle-o-notch fa-spin"></i></span>
          </div>
        </div>
      )
    } else {
      return (
        <div className="case case-edit">
          <div className="sidebar">
            <div class="sidebar-header">
              <span>DEATH RECORD CLIENT</span>
            </div>
            <ul className="nav nav-pills nav-stacked">
              <Link to={`${this.props.match.url}/provider`}>
                <li className="case-section-link">
                    <span className="case-section-link-text">Provider</span>
                    <span className="case-section-link-icon"><i className="fa fa-user-md" aria-hidden="true"></i></span>
                </li>
              </Link>
              <Link to={`${this.props.match.url}/facility`}>
                <li className="case-section-link">
                    <span className="case-section-link-text">Facility</span>
                    <span className="case-section-link-icon"><i className="fa fa-hospital-o" aria-hidden="true"></i></span>
                </li>
              </Link>
              <Link to={`${this.props.match.url}/patient`}>
                <li className="case-section-link">
                    <span className="case-section-link-text">Patient</span>
                    <span className="case-section-link-icon"><i className="fa fa-user" aria-hidden="true"></i></span>
                </li>
              </Link>
              <li className="case-siderbar-button">
                <Link to={`/cases/${this.props.match.params.caseId}/view`}>
                  <button
                    type="button"
                    className="button is-info is-medium is-fullwidth is-outlined"
                  >
                    View Case
                  </button>
                </Link>
              </li>
              <li className="case-siderbar-button">
                <button
                  type="button"
                  className="button is-danger is-medium is-disabled is-fullwidth"
                  disabled={!this.props.case.dirty}
                  onClick={this.submitForm}
                >
                  {this.props.case.updateInProgress ? (<div><span>Updating Case</span><span className="updating-ecr-icon"><i className="fa fa-circle-o-notch fa-spin"></i></span></div>) : 'Update Case'}
                </button>
              </li>
            </ul>
          </div>
          <div className="main">
            <nav className="navbar is-transparent" id="topnav">

              <div className="navbar-menu">

                <div className="navbar-end">
                  <div className="navbar-item">
                    <div className="field is-grouped">
                      <p className="control">
                        <Link to={`/`}>
                          <button
                            type="button"
                            className="button is-medium"
                          >
                            Back to Search
                          </button>
                        </Link>
                      </p>
                      <p className="control">
                        <Link to={`/cases`}>
                          <button
                            type="button"
                            className="button is-medium"
                          >
                            Cases
                          </button>
                        </Link>
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </nav>
            <div className="case-section-wrapper">
              <Switch>
                <Route
                  path={`${this.props.match.url}/provider`}
                  render={()=><CaseSection section="Provider" ref={(form) => {this.form=form;}}/>}
                />
                <Route
                  path={`${this.props.match.url}/facility`}
                  render={ () => <CaseSection section="Facility" ref={(form) => {this.form=form;}}/> }
                />
                <Route
                  path={`${this.props.match.url}/patient`}
                  render={()=><CaseSection section="Patient" ref={(form) => {this.form=form;}}/>}
                />
              </Switch>
            </div>
          </div>
        </div>
      );
    }
  }
}
