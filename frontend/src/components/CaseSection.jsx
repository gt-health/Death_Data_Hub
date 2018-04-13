import React from 'react';
import ReactDOM from 'react-dom';
import GridField from './GridField';
import Form from "react-jsonschema-form";
import schema from '../helpers/schema.json';
import { uiSchema } from '../helpers/uiSchema';

const customInputWidget = (props) => {
  return (
    <input type="text"
      className="input"
      value={props.value}
      required={props.required}
      onChange={(event) => props.onChange(event.target.value)} />
  );
}

const widgets = {
  customInputWidget: customInputWidget
};

const fields = {
  grid: GridField
};

function CustomFieldTemplate(props) {
  const {id, classNames, label, help, required, description, errors, children} = props;
  return (
    <div className={classNames}>
      <label className="label" htmlFor={id}>{label}</label>
      {description}
      <div className="control">{children}</div>
      {errors}
      {help}
    </div>
  );
}

function CustomArrayFieldTemplate(props) {
  return (
    <div className={props.className}>
      {props.items &&
        props.items.map(element => (
          <div key={element.index}>
            <div>{element.children}</div>
            <button class="button is-danger is-small" type="button" onClick={element.onDropIndexClick(element.index)}>{props.uiSchema['ui:options'].removeButtonText ? props.uiSchema['ui:options'].removeButtonText : 'Delete'}</button>
            <hr />
          </div>
        ))}
        {props.canAdd && <button class="button is-success is-small" type="button" onClick={props.onAddClick}>{props.uiSchema['ui:options'].addButtonText ? props.uiSchema['ui:options'].addButtonText : 'Add'}</button>}
    </div>
  );
}

function CustomObjectFieldTemplate({ TitleField, properties, title, description }) {
  return (
    <div>
      {/*<TitleField title={title} />*/}
      <div className="row">
        {properties.map(prop => (
          <div
            className="col-lg-2 col-md-4 col-sm-6 col-xs-12"
            key={prop.content.key}>
            {prop.content}
          </div>
        ))}
      </div>
      {description}
    </div>
  );
}

const log = (type) => console.log.bind(console, type);

export default class Case extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount() {
    // this.props.onGetCase(this.props.match.params.caseId);
  }

  componentWillMount() {}

  componentWillUnmount () {}

  componentDidUpdate(prevProps,prevState) {}

  onSubmit(data) {
    this.props.onSave(this.props.caseId, this.props.theCase);
  }

  onChange(data) {
    this.props.onChange(data.formData, this.props.section);
  }

  render() {
    console.log('*** rendering case section -- '+this.props.section+' -- component ***');
    console.log('formData: ',this.props.caseSection);
      return (
          <Form
            edit="false"
            schema={schema.properties[this.props.section]}
            formData={this.props.theCase[this.props.section]}
            uiSchema={uiSchema[this.props.section]}
            fields={fields}
            widgets={widgets}
            ArrayFieldTemplate={CustomArrayFieldTemplate}
            ObjectFieldTemplate={CustomObjectFieldTemplate}
            FieldTemplate={CustomFieldTemplate}
            onChange={(data) => this.onChange(data)}
            onSubmit={(data) => this.onSubmit(data)}
            onError={log("errors")}
          >
            <button ref={(btn) => {this.submitButton=btn;}} className="hidden" />
          </Form>
      );
  }
}
