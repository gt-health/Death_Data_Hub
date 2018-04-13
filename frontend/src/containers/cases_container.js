import { connect } from 'react-redux';
import Cases from '../components/Cases.jsx';

function mapStateToProps(state) {
  return {
    // user: state.user,
    // router: state.router
    params: state.search.params,
    cases: state.search.cases
  };
}

function mapDispatchToProps(dispatch) {
  return {
    // onChangeView: (redirectTo) => dispatch(changeView(redirectTo)),
  };
}

const casesContainer = connect(mapStateToProps, mapDispatchToProps)(Cases);

export default casesContainer;
