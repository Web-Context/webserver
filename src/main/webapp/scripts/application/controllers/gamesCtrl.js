'use strict';
/**
 * Games controller, provide all Games from back-end. 
 */
var app = angular.module("gamesrestserver",[])
	.controller("GameCtrl", ['$scope','$http',function($scope,$http){
	
		$http({method: 'GET', url: '/rest/games'})
			.success(function(data,status,headers,config){
				$scope.games = data.games;
			}).error(function(data,status,headers,config){
				alert("unable to retrieve data from server");
			});
		
	}]);