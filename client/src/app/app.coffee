"use strict"

app = angular.module('powah', ['templates.app'])

app.config(['$routeProvider', '$locationProvider', ($routeProvider, $locationProvider) ->
  $routeProvider.when('/home', {templateUrl: 'home/home.tpl.html', controller: 'HomeCtrl'})
])

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
