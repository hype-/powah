"use strict"

app = angular.module('powah')

app.controller('AuthenticationCtrl', ['$scope', 'AuthenticationService', ($scope, authenticationService) ->
  $scope.login = ->
    authenticationService.login($scope.credentials)
])
