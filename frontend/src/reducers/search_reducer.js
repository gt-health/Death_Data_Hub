export function searchReducer(state = {}, action) {
  switch(action.type) {
    case 'SEARCH_REQUESTED': {
      return {
        ...state,
        inProgress: true,
        error: '',
        success: ''
      };
    }
    case 'SEARCH_REJECTED': {
      return {
        ...state,
        inProgress: false,
        error: 'Problem searching cases.',
        success: ''
      };

    }
    case 'SEARCH_FULFILLED': {
      return {
        ...state,
        inProgress: false,
        error: '',
        success: 'Successfully retrieved cases.',
        params: action.payload.params,
        cases: action.payload.cases
      };
    }
    default:
      return state;
  }
}
