function buildQuery(params) {
  var esc = encodeURIComponent;
  console.log('params: ',params);
  var query = Object.keys(params)
      .map(function(k) {
        if (params[k]) {
          return (esc(k) + '=' + esc(params[k]));
        }
      })
      .join('&');
  return query;
}

export default buildQuery
