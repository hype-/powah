"use strict"

app = angular.module('powah')

app.factory('AuthenticationService', ['$http', '$location', ($http, $location) ->
  {
    login: (data) ->
      $http.post('/login', angular.toJson(data))
        .success ->
          $location.path('/home')
        .error ->
          $location.path('/')
  }
])
