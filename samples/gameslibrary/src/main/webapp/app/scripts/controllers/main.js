/**
 * 
 */
angular.module('gamesrestrserverUiApp').controller(
		'MainController',
		[ '$scope', '$route', '$routeParams', '$location',
				function($scope, $route, $routeParams, $location) {
					$scope.$route = $route;
					$scope.$location = $location;
					$scope.$route = $routeParams;
				} ]);
