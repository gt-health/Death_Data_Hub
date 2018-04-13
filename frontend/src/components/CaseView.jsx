import React from 'react';
import SchemaForm from 'react-jsonschema-form';
import GridField from './GridField';
import schema from '../helpers/schema.json';
import { Link } from 'react-router-dom';

export default class Case extends React.Component {

  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.onGetCase(this.props.match.params.caseId);
  }

  render() {
    console.log('*** rendering case view component ***');
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
        <div className="case-view">
          {/*<div className="sidebar">
            <nav className="navbar navbar-default">
              <div className="container-fluid">
                <div className="navbar-header">
                  <a className="navbar-brand" href="#">
                    STI Client
                  </a>
                </div>
              </div>
            </nav>
            <ul className="nav nav-pills nav-stacked">
              <li className="case-siderbar-button">
                <Link to={`/cases/${this.props.match.params.caseId}/edit/provider`}>
                  <button
                    type="button"
                    className="button is-primary is-medium is-fullwidth"
                  >
                    Edit Case
                  </button>
                </Link>
              </li>
            </ul>
          </div>*/}
          <div className="main">
            <nav className="navbar is-transparent" id="topnav">
              <div className="navbar-brand">
                <a className="navbar-item" href="https://bulma.io">
                  STI CLIENT
                </a>
                <div className="navbar-burger burger" data-target="navbarExampleTransparentExample">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>

              <div className="navbar-menu">

                <div className="navbar-end">
                  <div className="navbar-item">
                    <div className="field is-grouped">
                      <p className="control">
                        <Link to={`/cases/${this.props.match.params.caseId}/edit/provider`}>
                          <button
                            type="button"
                            className="button is-danger is-medium is-outlined"
                          >
                            Edit Case
                          </button>
                        </Link>
                      </p>
                      <p className="control">
                        <Link to={`/`}>
                          <button
                            type="button"
                            className="button is-medium"
                          >
                            Search
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
            <div className="case-section-view-wrapper">
              <div className="case-banner clearfix">
                <div className="patient-info">
                  <span>{this.props.case.raw.Patient.Name.family},&nbsp;{this.props.case.raw.Patient.Name.given}</span>
                  <span className="patient-sex-tag tag is-info">{this.props.case.raw.Patient.Sex}</span>
                </div>
                <div className="sending-app">
                  <span className="tag is-danger">Sending Application:&nbsp;{this.props.case.raw['Sending Application']}</span>
                </div>
              </div>
              <section className="section">
                <div className="columns">
                  <div className="column is-half">
                    <h3 className="title is-3">Provider</h3>
                    {this.props.case.raw.Provider.map(function(val, i){
                        return (
                          <table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
                          <tbody>
                            <tr>
                              <td>Address</td>
                              <td>{val.Address}</td>
                            </tr>
                            <tr>
                              <td>Country</td>
                              <td>{val.Country}</td>
                            </tr>
                            <tr>
                              <td>Email</td>
                              <td>{val.Email}</td>
                            </tr>
                            <tr>
                              <td>Facility</td>
                              <td>{val.Facility}</td>
                            </tr>
                            <tr>
                              <td>Fax</td>
                              <td>{val.Fax}</td>
                            </tr>
                            <tr>
                              <td>Name</td>
                              <td>{val.Name}</td>
                            </tr>
                            <tr>
                              <td>Phone</td>
                              <td>{val.Phone}</td>
                            </tr>
                            <tr>
                              <td>ID</td>
                              <td>{val.ID.type}&nbsp;{val.ID.value}</td>
                            </tr>
                          </tbody>
                          </table>
                        )
                    })}
                  </div>
                  <div className="column is-half">
                    <h3 className="title is-3">Facility</h3>
                    <table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
                      <tbody>
                        <tr>
                          <td>Address</td>
                          <td>{this.props.case.raw.Facility.Address}</td>
                        </tr>
                        <tr>
                          <td>Fax</td>
                          <td>{this.props.case.raw.Facility.Fax}</td>
                        </tr>
                        <tr>
                          <td>Hospital Unit</td>
                          <td>{this.props.case.raw.Facility.Hospital_Unit}</td>
                        </tr>
                        <tr>
                          <td>Name</td>
                          <td>{this.props.case.raw.Facility.Name}</td>
                        </tr>
                        <tr>
                          <td>Phone</td>
                          <td>{this.props.case.raw.Facility.Phone}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
                <div className="columns">
                  <div className="column is-half">
                    <h3 className="title is-3">Patient Identifying Info</h3>
                    <table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
                      <tbody>
                        <tr>
                          <td>Birth Date</td>
                          <td>{this.props.case.raw.Patient.Birth_Date}</td>
                        </tr>
                        <tr>
                          <td>Death Date</td>
                          <td>{this.props.case.raw.Patient.Death_Date}</td>
                        </tr>
                        <tr>
                          <td>Ethnicity</td>
                          <td>{this.props.case.raw.Patient.Ethnicity.Display}</td>
                        </tr>
                        <tr>
                          <td>Insurance</td>
                          <td>{this.props.case.raw.Patient.Insurance_Type.Display}</td>
                        </tr>
                        <tr>
                          <td>Name</td>
                          <td>{this.props.case.raw.Patient.Name.given}&nbsp;{this.props.case.raw.Patient.Name.family}</td>
                        </tr>
                        <tr>
                          <td>Occupation</td>
                          <td>{this.props.case.raw.Patient.Occupation}</td>
                        </tr>
                        <tr>
                          <td>Race</td>
                          <td>{this.props.case.raw.Patient.Race.Display}</td>
                        </tr>
                        <tr>
                          <td>Sex</td>
                          <td>{this.props.case.raw.Patient.Sex}</td>
                        </tr>
                        <tr>
                          <td>Pregnant</td>
                          <td>{this.props.case.raw.Patient.Pregnant}</td>
                        </tr>
                        <tr>
                          <td>Patient Class</td>
                          <td>{this.props.case.raw.Patient.PatientClass}</td>
                        </tr>
                        <tr>
                          <td>Address</td>
                          <td>{this.props.case.raw.Patient.Street_Address}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <div className="column is-half">
                    <h3 className="title is-3">Parent Guardian Info</h3>
                    {this.props.case.raw.Patient.Parents_Guardians.map(function(val, i){
                      return (
                        <table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
                          <tbody>
                            <tr>
                              <td>Name</td>
                              <td>{val.Name.given}&nbsp;{val.Name.family}</td>
                            </tr>
                            <tr>
                              <td>Email</td>
                              <td>{val.Email}</td>
                            </tr>
                            <tr>
                              <td>Phone</td>
                              <td>{val.Phone}</td>
                            </tr>
                          </tbody>
                        </table>
                      )
                    })}
                  </div>
                </div>
                <div className="columns">
                  <div className="column is-one-third">
                    <h3 className="title is-3">Travel History</h3>
                    <div class="notification is-warning">
                      No data to display.
                    </div>
                  </div>
                  <div className="column is-one-third">
                    <h3 className="title is-3">Immunization History</h3>
                    {this.props.case.raw.Patient.Immunization_History.map(function(val, i){
                      return (
                        <table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
                          <tbody>
                            <tr>
                              <td>Code</td>
                              <td>{val.Code}</td>
                            </tr>
                            <tr>
                              <td>Date</td>
                              <td>{val.Date}</td>
                            </tr>
                            <tr>
                              <td>System</td>
                              <td>{val.System}</td>
                            </tr>
                          </tbody>
                        </table>
                      )
                    })}
                  </div>
                  <div className="column is-one-third">
                    <h3 className="title is-3">Clinical Dates</h3>
                    <table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
                      <tbody>
                        <tr>
                          <td>Admission Date</td>
                          <td>{this.props.case.raw.Patient.Admission_DateTime}</td>
                        </tr>
                        <tr>
                          <td>Date Discharged</td>
                          <td>{this.props.case.raw.Patient.Date_Discharged}</td>
                        </tr>
                        <tr>
                          <td>Date of Onset</td>
                          <td>{this.props.case.raw.Patient.Date_Of_Onset}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>

                {/* DIAGNOSIS SECTION */}
                <h3 className="title is-3">Diagnosis</h3>
                <div className="notification is-info">{this.props.case.raw.Patient.Diagnosis.Display}</div>
                <div className="columns">
                  <div className="column">
                    <h4 className="title is-4">Medications</h4>
                    <table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
                      <thead>
                        <tr>
                          <th>Code</th>
                          <th>Date</th>
                          <th>Display</th>
                          <th>Dosage</th>
                          <th>Frequency</th>
                          <th>System</th>
                        </tr>
                      </thead>
                      {this.props.case.raw.Patient['Medication Provided'].map(function(val, i){
                        return (
                          <tbody>
                            <tr>
                              <td>{val.Code}</td>
                              <td>{val.Date}</td>
                              <td>{val.Display}</td>
                              <td>{val.Dosage.Value}{val.Dosage.Unit}</td>
                              <td>{val.Frequency}</td>
                              <td>{val.System}</td>
                            </tr>
                          </tbody>
                          )
                        })}
                    </table>
                  </div>
                </div>
                <div className="columns">
                  <div className="column">
                  <h4 className="title is-4">Lab Results</h4>
                  {this.props.case.raw.Patient['Lab_Order_Code'].map(function(val, i){
                    return (
                      <div>
                        <p class="lab-name"><strong>Lab:</strong>&nbsp;{val.Display}</p>
                        <table className="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
                          <thead>
                            <tr>
                              <th>Code</th>
                              <th>Date</th>
                              <th>Display</th>
                              <th>System</th>
                              <th>Value</th>
                            </tr>
                          </thead>
                          {val.Laboratory_Results.map(function(val, i){
                            return (
                              <tbody>
                                <tr>
                                  <td>{val.Code}</td>
                                  <td>{val.Date}</td>
                                  <td>{val.Display}</td>
                                  <td>{val.System}</td>
                                  <td>{val.Value}{val.Unit.Display}</td>
                                </tr>
                              </tbody>
                              )
                            })}
                        </table>
                      </div>
                      )
                    })}
                  </div>
                </div>
                <div className="columns">
                  <div className="column">
                    <h4 className="title is-4">Symptoms</h4>
                    <div class="tags">
                    {this.props.case.raw.Patient.Symptoms.map(function(val, i){
                      return (
                        <span class="tag">{val.Display}</span>
                      )
                    })}
                    </div>
                  </div>
                </div>
              </section>
            </div>
          </div>
        </div>
      );
    }
  }

}
