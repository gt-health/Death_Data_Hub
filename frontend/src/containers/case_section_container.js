import { connect } from 'react-redux';
import CaseSection from '../components/CaseSection.jsx';
// import { updateCase } from '../actions/update_case.js';
import { onChange } from '../actions/on_change.js';
import { onSave } from '../actions/on_save.js';

function mapStateToProps(state, ownProps) {
  return {
    // user: state.user,
    // router: state.router
    // caseSection: state.case.raw[ownProps.section],
    caseId: state.case.raw.Id,
    theCase: state.case.raw
  };
}

function mapDispatchToProps(dispatch) {
  return {
    onChange: (formData, section) => dispatch(onChange(formData, section)),
    onSave: (id, formData) => dispatch(onSave(id, formData))
    // onChangeView: (redirectTo) => dispatch(changeView(redirectTo)),
    // onUpdateCase: (data) => dispatch(updateCase(data))
  };
}

const caseSectionContainer = connect(mapStateToProps, mapDispatchToProps, null, { withRef: true })(CaseSection);

export default caseSectionContainer;
