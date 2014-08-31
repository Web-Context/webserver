'use strict';

describe('Service: Gamesservice', function () {

  // load the service's module
  beforeEach(module('gamesrestrserverUiApp'));

  // instantiate service
  var Gamesservice;
  beforeEach(inject(function (_Gamesservice_) {
    Gamesservice = _Gamesservice_;
  }));

  it('should do something', function () {
    expect(!!Gamesservice).toBe(true);
  });

});
