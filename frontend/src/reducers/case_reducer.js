import { identifiers } from '../helpers/identifiers.js';

const initialState = {
	inProgress: true,
//	raw: {
//		Patient: {
//			Name: {
//				
//			}
//		}, 
//		Provider: [],
//		Facility: {}
//	}
}

export function caseReducer(state = initialState, action) {
  switch(action.type) {
    case 'GET_CASE_REQUESTED': {
      return {
        ...state,
        inProgress: true,
        error: '',
        success: ''
      };
    }
    case 'GET_CASE_REJECTED': {
      return {
        ...state,
        inProgress: false,
        error: 'Error in getting case.'
      };
    }
    case 'GET_CASE_FULFILLED': {
      var fields = {};
      identifiers.forEach((e) => {
        var res = e.split('__');
        if (res.length > 1) {
          fields[e] = action.theCase[res[0]][res[1]];
        } else {
          fields[e] = action.theCase[res[0]];
        }
      });

      return {
        ...state,
        raw: action.theCase,
        inProgress: false,
        success: 'Got case',
        fields: fields,
        dirty: false
      }
    }
    case 'ON_CHANGE': {
      return {
        ...state,
        raw: {
          ...state.raw,
          [action.payload.key]: action.payload.formData
        },
        dirty: true
      }
    }
    case 'SAVE_CASE_REQUESTED': {
      return {
        ...state,
        updateInProgress: true,
        error: '',
        success: ''
      }
    }
    case 'SAVE_CASE_FULFILLED': {
      return {
        ...state,
        updateInProgress: false,
        error: '',
        success: 'Case saved.',
        dirty: false
      }
    }
    case "SAVE_CASE_REJECTED": {
      return {
        ...state,
        updateInProgress: false,
        error: 'Problem saving case.',
        success: '',
        dirty: false
      }
    }
    default:
      return state;
  }
}
