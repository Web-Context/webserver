'use strict';
/**
 * Games controller, provide all Games from back-end.
 */
var app = angular.module("gamesrestserver.controllers", ['ngRoute'])
.controller("GamesCtrl",['$scope','$http','$route','$routeParams', 'gamesService',
         function($scope, $http, $route, $routeParams, gamesService) {	
			$scope.routeParams = $routeParams;
			// Retrieve games and platforms
			gamesService.findGames($routeParams.platform)
				.success(function(data, status, headers, config) {
					$scope.language = window.navigator.userLanguage || window.navigator.language;
					$scope.games = data.games;
					$scope.platforms = data.platforms;

				}).error(function(data, status, headers, config) {
					alert("unable to retrieve data from server");
				});
		}]);
