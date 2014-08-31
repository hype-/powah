(function () {
  'use strict';

  var app = angular.module('powah');

  app.factory('CookieParser', ['$cookies', function ($cookies) {
    var parseCookie = function () {
      var data = {};
      var rawCookie = $cookies['PLAY_SESSION'];

      if ($cookies['PLAY_SESSION']) {
        var rawData = rawCookie.substring(rawCookie.indexOf('-') + 1, rawCookie.length - 1);

        rawData.split('&').forEach(function (rawPair) {
          var pair = rawPair.split('=');

          data[pair[0]] = pair[1];
        });
      }

      return data;
    };

    return {
      get: function (key) {
        return parseCookie()[key];
      }
    };
  }]);
})();
