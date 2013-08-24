"use strict"

app = angular.module('powah')

app.factory(
  'AuthenticationService',
  ['$http', '$location', 'SessionService', ($http, $location, sessionService) ->
    {
      login: (credentials) ->
        $http.post('/login', angular.toJson(credentials))
          .success ->
            sessionService.setUsername(credentials.username)
            $location.path('/home')
          .error ->
            $location.path('/')
    }
  ]
)
