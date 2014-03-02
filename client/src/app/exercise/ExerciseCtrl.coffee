"use strict"

app = angular.module('powah')

app.controller(
  'ExerciseCtrl',
  [
    '$scope', '$routeParams', 'SessionService', 'RepSetService',
    ($scope, $routeParams, sessionService, repSetService) ->
      $scope.user = sessionService.getUser()

      repSetService.getRepSets($routeParams.date)

      $scope.addRepSet = ->
        repSetService.addRepSet($scope.newRepSet, $routeParams.date)
  ]
)
