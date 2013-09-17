"use strict"

app = angular.module('powah')

app.factory(
  'SessionService',
  [() ->
    user = {
      username: null
    }

    {
      setUsername: (username) -> user.username = username
      getUser: -> user
      hasUser: -> user.username != null
    }
  ]
)
