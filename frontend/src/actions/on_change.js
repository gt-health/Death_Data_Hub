export function onChange(formData, key) {
  return dispatch => {
    dispatch(onChangeAction(formData, key));
  }
}

function onChangeAction(formData, key) {
  return {
    type: 'ON_CHANGE',
    payload: { formData: formData, key: key }
  };
}
