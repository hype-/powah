describe 'authentication controller', ->
  $controller = undefined
  $scope = undefined
  $httpBackend = undefined
  $location = undefined

  beforeEach(module('powah'))
  beforeEach(inject ($injector) ->
    $controller = $injector.get('$controller')
    $scope = $injector.get('$rootScope')
    $httpBackend = $injector.get('$httpBackend')
    $location = $injector.get('$location')
  )

  createController = ($scope) ->
    $controller('AuthenticationCtrl', {$scope: $scope})

  afterEach ->
    $httpBackend.verifyNoOutstandingExpectation()
    $httpBackend.verifyNoOutstandingRequest()

  it 'authenticates with valid credentials', ->
    $scope.credentials = {username: 'xoo', password: 'bar'}

    authenticationCtrl = createController($scope)

    $httpBackend.expectPOST(
      '/login',
      """{"username":"xoo","password":"bar"}"""
    ).respond(200)

    $scope.login()

    $httpBackend.flush()

    expect($location.path()).toBe('/home')

  it 'goes to / when login fails', ->
    authenticationCtrl = createController($scope)

    $httpBackend.expectPOST('/login').respond(401)

    $scope.login()

    $httpBackend.flush()

    expect($location.path()).toBe('/')
