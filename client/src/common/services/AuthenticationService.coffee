"use strict"

app = angular.module('powah')

app.factory(
  'AuthenticationService',
  [
    '$http', '$location', 'SessionService', 'CookieParser',
    ($http, $location, sessionService, cookieParser) ->
      {
        login: (credentials) ->
          $http.post('/login', angular.toJson(credentials))
            .success ->
              sessionService.setUsername(credentials.username)
              $location.path('/home')
            .error ->
              $location.path('/')

        isLoggedIn: () ->
          sessionService.hasUser()

        tryToAuthenticateWithCurrentUser: ->
          currentUser = cookieParser.get('username')

          if !sessionService.hasUser() && currentUser
            sessionService.setUsername(currentUser)
      }
  ]
)
