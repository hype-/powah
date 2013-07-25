"use strict"

app = angular.module('powah', [])

app.controller('EntryCtrl', ['$scope', '$http', ($scope, $http) ->
  $scope.entries = []

  $http.get('entries').success (response) ->
    $scope.entries = response.data

  $scope.addEntry = ->
    entry = {name: $scope.entryName}

    $scope.entries.push(entry)
    $scope.entryName = ''
    $http.post('entries', angular.toJson(entry))
])
