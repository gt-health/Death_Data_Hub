import { connect } from 'react-redux';
import Search from '../components/Search.jsx';
import { search } from '../actions/search.js';

function mapStateToProps(state) {
  return {
    // user: state.user,
    // router: state.router
    search: state.search
  };
}

function mapDispatchToProps(dispatch) {
  return {
    onSearch: (params) => dispatch(search(params))
    // onChangeView: (redirectTo) => dispatch(changeView(redirectTo)),
  };
}

const searchContainer = connect(mapStateToProps, mapDispatchToProps)(Search);

export default searchContainer;
