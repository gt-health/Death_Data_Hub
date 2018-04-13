import React from 'react';
import ReactDOM from 'react-dom';
// import { DateRangePicker, SingleDatePicker, DayPickerRangeController } from 'react-dates';
import { Link } from 'react-router-dom';

export default class Search extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
    };
  }

  handleInputChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;

    this.setState({
      [name]: value
    });
  }

  handleSubmit() {
    const params = this.state;
    console.log('search params: ',params);
    this.props.onSearch(params);
  }

  render() {
    var button;
    if (this.props.search.error) {
      button = this.props.search.error;
    } else if (this.props.search.inProgress) {
      button = 'In progress...';
    } else {
      button = 'Search';
    }
    console.log('*** rendering search component ***');
    return (
      <div className="search">
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
        </nav>
        <section className="section">
          <div className="container">
            <div className="columns">
              <div className="column">
                <div className="field">
                  <label className="label">Case ID</label>
                  <div className="control">
                    <input
                      id="inputCaseId"
                      className="input"
                      type="text"
                      placeholder="Case ID"
                      name="id"
                      value={this.state.id}
                      onChange={(e) => this.handleInputChange(e)}
                    />
                  </div>
                </div>
              </div>
              {/*<div className="col-md-6 col-sm-12">
                <div className="form-group">
                  <label for="inputDateRange">Date Range</label>
                  <div>
                    <DateRangePicker
                      startDate={this.state.dateStart}
                      endDate={this.state.dateEnd}
                      onDatesChange={({ dateStart, dateEnd }) => this.setState({ dateStart, dateEnd })}
                      focusedInput={this.state.focusedInput}
                      onFocusChange={focusedInput => this.setState({ focusedInput })}
                    />
                  </div>
                </div>
              </div>*/}
            </div>
            <div className="columns">
              <div className="column">
                <div className="field">
                  <label className="label">Last name</label>
                  <div className="control">
                    <input
                      id="inputLastName"
                      className="input"
                      type="text"
                      placeholder="Last name"
                      name="lastName"
                      value={this.state.lastName}
                      onChange={(e) => this.handleInputChange(e)}/>
                  </div>
                </div>
              </div>
              <div className="column">
                <div className="field">
                  <label className="label">First name</label>
                  <div className="control">
                  <input
                    id="inputFirstName"
                    className="input"
                    type="text"
                    placeholder="First name"
                    name="firstName"
                    value={this.state.firstName}
                    onChange={(e) => this.handleInputChange(e)}
                    />
                  </div>
                </div>
              </div>
            </div>
            <div className="columns">
              <div className="column">
                <div className="field">
                  <label className="label">Zip Code</label>
                  <div className="control">
                    <input
                      id="inputZipCode"
                      className="input"
                      type="text"
                      placeholder="Zip Code"
                      name="zipCode"
                      value={this.state.zipCode}
                      onChange={(e) => this.handleInputChange(e)}
                    />
                  </div>
                </div>
              </div>
              <div className="column">
                <div className="field">
                  <label className="label">Diagnosis</label>
                  <div class="select">
                  <select value={this.state.diagnosis} onChange={(e) => this.handleInputChange(e)} name="diagnosis" id="inputDiagnosis">
                    <option>Select diagnosis</option>
                    <option value="toefungus">Toe Fungus</option>
                  </select>
                  </div>
                </div>
              </div>
            </div>
            {this.props.search.error}
            <div className="row">
              <div className="col-md-6 col-sm-12">
                <button className="button is-primary is-medium" onClick={() => this.handleSubmit()}>{button}</button>
              </div>
            </div>
          </div>
        </section>
      </div>
    );
  }
}
