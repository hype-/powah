app = angular.module('powahTest', ['powah', 'ngMockE2E']);

app.run ($httpBackend) ->
  $httpBackend.whenPOST('/login').respond(200)
