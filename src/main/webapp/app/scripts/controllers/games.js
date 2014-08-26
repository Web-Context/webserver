'use strict';

angular.module('gamesrestrserverUiApp')
  .controller('GamesCtrl', ['$scope','$http','$route','$routeParams','$log','Gamesservice',
         function($scope, $http, $route, $routeParams, $log, Gamesservice) {
			$scope.routeParams = $routeParams;
			// Retrieve games and platforms
			var platform = $routeParams.platform?$routeParams.platform:"";
			var data = Gamesservice.findGames(platform);
			$scope.language = window.navigator.userLanguage || window.navigator.language;
			data.then(function(payload){
				
				$scope.games = payload.data.games;
				$scope.platforms = payload.data.platforms;
				$log.debug('retrieve '+payload.data.games.length+' games');
				$log.debug('retrieve '+payload.data.platforms.length+' platforms');
				
			}, function(errorPayload){
				$log.error('failure loading games',errorPayload);
			})
		}]);