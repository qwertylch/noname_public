const request = (function () {


	function get(path, data, callback, error) {
		$.ajax({
			type: 'GET',
			url: path + data,
			dataType: 'json',
			success: function (result, status, xhr) {
				if (callback) {
					callback(result);
				}
			},
			error: function (xhr, status, er) {
				if (error) {
					error(er);
				}
			}
		});
	}
	
	function postWithJson(path, data, callback, error) {
	  $.ajax({
	    type: 'POST',
	    url: path,
	    data: JSON.stringify(data),
	    contentType: 'application/json', 
	    dataType: 'text', 
	    success: function(result, status, xhr) {
	      if(callback) {
	        callback(result);
	      }
	    },
	    error : function(xhr, status, er){
	      console.log(er);
	      if(error) {
	        error(er);
	      }
	    }
	  });
	}
	
	function postWithParam(path, data, callback, error) {
	  $.ajax({
	    type: 'POST',
	    url: path + data,
	    dataType: 'text', 
	    success: function(result, status, xhr) {
	      if(callback) {
	        callback(result);
	      }
	    },
	    error : function(xhr, status, er){
	      if(error) {
	        error(er);
	      }
	    }
	  });
	}
	
	
	
	function put(path, data, callback, error) {
	  $.ajax({
	    type: 'PUT',
	    url: path,
	    data: JSON.stringify(data),
	    contentType: 'application/json', 
	   	dataType: 'text', 
	    success: function(result, status, xhr) {
	      if(callback) {
	        callback(result);
	      }
	    },
	    error : function(xhr, status, er){
	      console.log(er);
	      if(error) {
	        error(er);
	       
	      }
	    }
	  });
	}
	
	
	

	function remove(path, data, callback, error) {
		$.ajax({
			type: 'DELETE',
			url: path + data,
			dataType: 'text', 
			success: function (result, status, xhr) {
				if (callback) {
					callback(result);
				}
			},
			error: function (xhr, status, er) {
				if (error) {
					error(er);
				}
			}
		});
	}

	return {
		get: get,
		postWithJson: postWithJson,
		postWithParam: postWithParam,
		put: put,
		remove: remove
	}

})();