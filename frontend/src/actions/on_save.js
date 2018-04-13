import axios from 'axios';

export function onSave(caseId, theCase) {
  return dispatch => {
    dispatch(saveCaseRequestedAction());
    return axios.put(process.env.REACT_APP_API_URL_CASE+'?id='+caseId, theCase )
      .then((response) => {
        // var theCase = response.data;
        console.log('case successfully saved.');
        dispatch(saveCaseFulfilledAction());
      })
      .catch((error) => {
        console.log('error: ',error);
        dispatch(saveCaseRejectedAction());
      });
  }
}

function saveCaseRequestedAction() {
  return {
    type: 'SAVE_CASE_REQUESTED'
  };
}

function saveCaseRejectedAction() {
  return {
    type: 'SAVE_CASE_REJECTED'
  }
}

function saveCaseFulfilledAction() {
  return {
    type: 'SAVE_CASE_FULFILLED'
  };
}
