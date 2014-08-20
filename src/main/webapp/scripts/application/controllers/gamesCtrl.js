'use strict';
/**
 * Games controller, provide all Games from back-end.
 */
var app = angular.module("gamesrestserver", []).controller(
		"GamesCtrl",
		[
				'$scope',
				'$http',
				'$route',
				'$routeParams',
				'$location',
				function($scope, $http, $route, $routeParams, $location) {
					$http({
						method : 'GET',
						url : '/rest/games'+($routeParams['code']?"?platform="+$routeParams['code']:"")
					}).success(
							function(data, status, headers, config) {
								$scope.language = window.navigator.userLanguage
										|| window.navigator.language;
								$scope.games = data.games;
								$scope.platforms = data.platforms;

							}).error(function(data, status, headers, config) {
						alert("unable to retrieve data from server");
					});

				} ]);
