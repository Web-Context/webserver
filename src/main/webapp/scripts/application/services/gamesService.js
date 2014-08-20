'user strict';
angular.module('gamesrestserver.services',[])
	.factory('gamesService', ['$http',function($http){
		var gameService = {
				findGames : function(platform){
					return $http({method: 'JSONP', url:'http://localhost:8888/rest/games?platform='+platform});
				},
				listPlatform:  function(){
					return $http({method: 'JSONP', url:'http://localhost:8888/rest/games/platforms'+platform});
				}
				
		};
		return gameService;
	}]);