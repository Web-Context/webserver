'use strict';

angular.module('gamesrestrserverUiApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute'
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/games.html',
        controller: 'GamesCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
