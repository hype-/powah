"use strict"

app = angular.module('powah')

app.factory(
  'CookieParser',
  ['$cookies', ($cookies) ->
    parseCookie = ->
      data = {}

      rawCookie = $cookies['PLAY_SESSION']

      if $cookies['PLAY_SESSION']
        rawData = rawCookie.substring(rawCookie.indexOf('-') + 1, rawCookie.length - 1)

        _.each(rawData.split('&'), (rawPair) ->
          pair = rawPair.split('=')
          data[pair[0]] = pair[1]
        )

      data

    {
      get: (key) ->parseCookie()[key]
    }
  ]
)
