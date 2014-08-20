'use strict';
/**
 * 
 */
var app = angular.module("gamesrestserver",[
    "GameCtrl"
]);
app.config(function($httpProvider){
	$httpProvider.defaults.headers.get = {
			'API-KEY' : '123456789ABCDEF'
		};
});