'use strict';
/**
 * Main application.
 */
var app = angular.module("gamesrestserver", [ 'GamesCtrl', 'ngRoute']);

// Add a Wait factory
app.factory('WaitInit', ['$timeout', function ($timeout) {
    return $timeout(function () {}, 3000);
}]);

app.config(['$routeProvider','$locationProvider',function( $routeProvider, $locationProvider) {
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
