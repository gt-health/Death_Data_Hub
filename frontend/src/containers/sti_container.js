import { connect } from 'react-redux';
import StiApp from '../components/StiApp.jsx';

function mapStateToProps(state) {
  return {
    // user: state.user,
    // router: state.router
  };
}

function mapDispatchToProps(dispatch) {
  return {
    // onChangeView: (redirectTo) => dispatch(changeView(redirectTo)),
  };
}

const stiContainer = connect(mapStateToProps, mapDispatchToProps)(StiApp);

export default stiContainer;
