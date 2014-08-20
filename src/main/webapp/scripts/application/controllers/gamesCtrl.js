'use strict';
/**
 * Games controller, provide all Games from back-end. 
 */
var app = angular.module("gamesrestserver",[]);

app.controller("GamesCtrl", function($scope,$http,$get){
	
	$httpProvider.defaults.headers.get = { 'API-KEY' : '123456789ABCDEF' };
	
	$http({method: 'GET', url: '/rest/games'}).success(function(data,status,headers,config){
		$scope.games = data;
	}).error(function(data,status,headers,config){
		alert("unable to retrieve data from server");
	});
	
});