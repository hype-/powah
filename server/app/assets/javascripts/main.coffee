'use strict'

window.EntryCtrl = ($scope, $http) ->
  $scope.entries = []

  $http.get('entries').success (response) ->
    $scope.entries = response.data

  $scope.addEntry = ->
    entry = {name: $scope.entryName}
    console.log entry

    $scope.entries.push(entry)
    $scope.entryName = ''
    $http.post('entries', angular.toJson(entry))
