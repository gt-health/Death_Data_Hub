import React from 'react';
import ObjectField from 'react-jsonschema-form/lib/components/fields/ObjectField';
import {retrieveSchema} from "react-jsonschema-form/lib/utils";
import {Grid, Row, Col} from 'react-bootstrap';

class GridField extends ObjectField {
  constructor(props) {
    super(props);
  }

  render() {
    const {
      formData,
      uiSchema,
      errorSchema,
      idSchema,
      name,
      required,
      disabled,
      readonly,
      onBlur
    } = this.props;
    const {definitions, fields, formContext} = this.props.registry;
    const {SchemaField, TitleField, DescriptionField} = fields;
    // const {TitleField, DescriptionField} = fields;
    const schema = retrieveSchema(this.props.schema, definitions);
    const title = (schema.title === undefined) ? name : schema.title;
    const order = uiSchema['ui:options'].order;
    // const log = (type) => console.log.bind(console, type);
    return (
      <fieldset>
        {/*{title ? <TitleField
            id={`${idSchema.$id}__title`}
            title={title}
            required={required}
            formContext={formContext}/> : null}*/}
        {schema.description ?
          <DescriptionField
            id={`${idSchema.$id}__description`}
            description={schema.description}
            formContext={formContext}/> : null}
        {
          order.map((row, index) => {
            return (
              <div className="columns" key={index}>
                {
                  Object.keys(row).map((name, index) => (
                    <div className="column">
                    {/*<Col {...row[name]} key={index}>*/}
                      <SchemaField
                                   name={name}
                                   required={this.isRequired(name)}
                                   schema={schema.properties[name]}
                                   uiSchema={uiSchema[name]}
                                   errorSchema={errorSchema[name]}
                                   idSchema={idSchema[name]}
                                   formData={formData[name]}
                                   onChange={this.onPropertyChange(name)}
                                   onBlur={onBlur}
                                   registry={this.props.registry}
                                   disabled={disabled}
                                   readonly={readonly}/>
                    </div>
                  ))
                }
              </div>
            );
          })
        }</fieldset>
    );
  }
};

export default GridField
