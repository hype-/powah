"use strict"

app = angular.module('powah', ['ngCookies', 'templates.app'])

app.config(['$routeProvider', '$locationProvider', ($routeProvider, $locationProvider) ->
  $routeProvider.when('/home', {templateUrl: 'home/home.tpl.html', controller: 'HomeCtrl'})

  $routeProvider.when(
    '/days/:date/exercises',
    {templateUrl: 'exercise/exercise.tpl.html', controller: 'ExerciseCtrl'}
  )
])

app.run([
  '$rootScope', '$location', 'AuthenticationService',
  ($rootScope, $location, authenticationService) ->
    authenticationService.tryToAuthenticateWithCurrentUser()

    $rootScope.$on('$routeChangeStart', (event, next, current) ->
      if (!authenticationService.isLoggedIn())
        $location.path('/')
    )
])
