'use strict';

angular.module('gamesrestrserverUiApp')
.factory('Gamesservice', ['$http','$q',function($http,$q){
	return {
		findGames : function(platform){
			var deferred = $q.defer();
			$http.get('http://localhost:8888/rest/games'+(platform!=undefined?'&platform='+platform:''))
				.success(function (data, status, headers, config) {				
					deferred.resolve({data:data, headers: headers });
				})
				.error(function (data, status, headers, config) {
					deferred.reject(data);
				});
			return deferred.promise;
		},
		find : function(gameId){
			var deferred = $q.defer();
			$http.get('http://localhost:8888/rest/games/?id='+gameId)
				.success(function (data, status, headers, config) {				
					deferred.resolve({data:data, headers: headers });
				})
				.error(function (data, status, headers, config) {
					deferred.reject(data);
				});
			return deferred.promise;
		},
		listPlatform:  function(){
			var deferred = $q.defer();
			$http.get('http://localhost:8888/rest/games/platforms/')
				.success(function (data, status, headers, config) {
					deferred.resolve({data:data, status:status});
				})
				.error(function (data, status, headers, config) {
					deferred.reject(data);
				});
			return deferred.promise;
		}
	};
}]);