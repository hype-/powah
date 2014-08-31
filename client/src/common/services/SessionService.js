(function () {
  'use strict';

  var app = angular.module('powah');

  app.factory('SessionService', [function () {
    var user = {
      username: null
    };

    return {
      setUsername: function (username) {
        user.username = username;
      },

      getUser: function () {
        return user;
      },

      hasUser: function () {
        return user.username !== null;
      }
    };
  }]);
})();
