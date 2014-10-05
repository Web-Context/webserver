'use strict';

angular.module('gamesrestrserverUiApp',
		[ 'ngCookies', 'ngResource', 'ngSanitize', 'ngRoute' ]).config(
		function($routeProvider,$locationProvider) {
			$routeProvider.when('/', {
				templateUrl : 'views/games.html',
				controller : 'GamesCtrl'
			}).when('/game/:gameId', {
				templateUrl : 'views/game.html',
				controller : 'GamesCtrl'
			}).when('/game/:gameId/platform/:platform', {
				templateUrl : 'views/game.html',
				controller : 'GamesCtrl'
			}).otherwise({
				redirectTo : '/'
			});

			// configure html5 to get links working on jsfiddle
			//$locationProvider.html5Mode(true);
		});
