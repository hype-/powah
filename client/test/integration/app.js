(function() {
  'use strict';

  var app = angular.module('powahTest', ['powah', 'ngMockE2E']);

  app.run(function ($httpBackend) {
    $httpBackend.whenPOST('/login').respond(200);
  });
})();
