"use strict"

app = angular.module('powah')

app.controller(
  'HomeCtrl',
  ['$scope', 'SessionService', ($scope, sessionService) ->
    $scope.user = sessionService.getUser()
    $scope.currentDate = new Date()
  ]
)
