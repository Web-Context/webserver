'use strict';
/**
 * Main application.
 */
var app = angular.module("gamesrestserver", [ 'ngRoute','gamesrestserver.services','gamesrestserver.controllers', ]);

// Add a Wait factory
app.factory('WaitInit', ['$timeout', function ($timeout) {
    return $timeout(function () {}, 3000);
}]);

app.config(['$routeProvider','$locationProvider','$routeParamsProvider',function( $routeProvider, $locationProvider,$routeParamsProvider) {
	$httpProvider.defaults.headers.get = {
		'API-KEY' : '123456789ABCDEF'
	};
	$routeProvider.when('/platform/:code', {
		templateUrl : 'views/games.html',
		controller : 'GamesCtrl',
		resolve : {wait: 'WaitInit'}
	}).when('/games/:id', {
		templateUrl : 'views/games.html',
		controller : 'GamesCtrl',
		resolve : {wait: 'WaitInit'}
	}).otherwise({
		redirectTo: '/',
		templateUrl : 'views/games.html',
		controller : 'GamesCtrl'		
	});
	
	$locationProvider.html5Mode(true);

}]);
