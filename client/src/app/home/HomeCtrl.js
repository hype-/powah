(function () {
  'use strict';

  var app = angular.module('powah');

  app.controller(
    'HomeCtrl',
    ['$scope', 'SessionService', function ($scope, sessionService) {
      $scope.user = sessionService.getUser();
      $scope.currentDate = new Date();
    }]
  );
})();
