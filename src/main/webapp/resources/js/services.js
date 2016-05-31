'use strict';

/* Services */

var AppServices = angular.module('newsApp.services', ['ngResource']);

AppServices.factory('UserService', function($resource) {
	
	return $resource('user/:action', {},
			{
				authenticate: {
					method: 'POST',
					params: {'action' : 'authenticate'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				},	
			}
		);
});