(function () {
  'use strict';

  var app = angular.module('powah');

  app.controller(
    'ExerciseCtrl',
    [
      '$scope', '$routeParams', 'SessionService', 'RepSetService',
      function ($scope, $routeParams, sessionService, repSetService) {
        $scope.user = sessionService.getUser();

        repSetService.getRepSets($routeParams.date);

        $scope.addRepSet = function () {
          repSetService.addRepSet($scope.newRepSet, $routeParams.date);
        };
      }
    ]
  );
})();
