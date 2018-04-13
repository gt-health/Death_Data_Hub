import { combineReducers } from 'redux';
import { searchReducer } from './search_reducer';
import { casesReducer } from './cases_reducer';
import { caseReducer } from './case_reducer';

const rootReducer = combineReducers({
  search: searchReducer,
  cases: casesReducer,
  case: caseReducer
});

export default rootReducer;
