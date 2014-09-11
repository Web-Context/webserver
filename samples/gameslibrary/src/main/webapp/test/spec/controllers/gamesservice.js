'use strict';

describe('Controller: GamesserviceCtrl', function () {

  // load the controller's module
  beforeEach(module('gamesrestrserverUiApp'));

  var GamesserviceCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    GamesserviceCtrl = $controller('GamesserviceCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
