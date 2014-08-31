(function () {
  'use strict';

  var app = angular.module('powah');

  app.factory(
    'AuthenticationService',
    [
      '$http', '$location', 'SessionService', 'CookieParser',
      function ($http, $location, sessionService, cookieParser) {
        return {
          login: function (credentials) {
            return $http.post('/login', angular.toJson(credentials)).success(function () {
              sessionService.setUsername(credentials.username);

              $location.path('/home');
            }).error(function () {
              $location.path('/');
            });
          },

          isLoggedIn: function () {
            return sessionService.hasUser();
          },

          tryToAuthenticateWithCurrentUser: function () {
            var currentUser = cookieParser.get('username');

            if (!sessionService.hasUser() && currentUser) {
              sessionService.setUsername(currentUser);
            }
          }
        };
      }
    ]
  );
})();
