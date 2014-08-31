(function () {
  'use strict';

  var app = angular.module('powah');

  app.controller(
    'AuthenticationCtrl',
    [
      '$scope', 'AuthenticationService',
      function ($scope, authenticationService) {
        $scope.login = function () {
          authenticationService.login($scope.credentials);
        };
      }
    ]
  );
})();
