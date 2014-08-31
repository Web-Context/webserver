'use strict';

angular.module('gamesrestrserverUiApp')
	.controller('GamesCtrl',[
		'$scope',
		'$http',
		'$route',
		'$routeParams',
		'$log',
		'Gamesservice',
	function($scope, $http, $route, $routeParams, $log, Gamesservice) {
		$scope.params = $routeParams;
		// Retrieve games and platforms
		var platform = $scope.params.platform ? $scope.params.platform : "";
		var gameId = $scope.params.gameId ? $scope.params.gameId	: "";
		var data =  [];
		
		// if a platform is provided
		if(platform!=""){
			data = Gamesservice.findGames(platform);
		// if a game ID is provided
		}else if(gameId!=""){
			data = Gamesservice.find(gameId);
		// nothing provided
		}else{
			data = Gamesservice.findGames();
		}
		$scope.language = window.navigator.userLanguage
				|| window.navigator.language;
		data.then(
				function(payload) {	
					$scope.games = payload.data.games;
					$scope.platforms = payload.data.platforms;
					$log.debug('retrieve ' + payload.data.games.length + ' games');
					$log.debug('retrieve ' + payload.data.platforms.length + ' platforms');
				},
				function(errorPayload) {
					$log.error('failure loading games', errorPayload);
			})
		} ] 
	);
