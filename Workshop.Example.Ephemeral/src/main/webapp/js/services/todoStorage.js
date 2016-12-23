/*global todomvc */
'use strict';

/**
 * Services that persists and retrieves TODOs from localStorage
 */
todomvc.factory('todoStorage', function () {
	var STORAGE_ID = 'todos-angularjs';
	$.ajaxSetup({async:false});
	
	return {
		get: function () {
			var todos = [];
			$.get("/todos", function (data) {
				todos = JSON.parse(data);
			});
			return todos;
		},

		put: function (todos, onError) {
			$.ajax({
				type: "POST",
				url:"/todos",
				data:{todos:JSON.stringify(todos)},
				async: "false",
				error: onError
			});
		}
	};
});
