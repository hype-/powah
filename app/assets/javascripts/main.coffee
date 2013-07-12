'use strict'

window.EntryCtrl = ($scope, $http) ->
  $http.get('entries').success (response) ->
    $scope.entries = response.data
