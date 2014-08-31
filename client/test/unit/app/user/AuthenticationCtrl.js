describe('authentication controller', function () {
  var $controller, $httpBackend, $location, $scope, createController;

  beforeEach(module('powah'));
  beforeEach(inject(function ($injector) {
    $controller = $injector.get('$controller');
    $scope = $injector.get('$rootScope');
    $httpBackend = $injector.get('$httpBackend');
    $location = $injector.get('$location');
  }));

  createController = function ($scope) {
    return $controller('AuthenticationCtrl', {
      $scope: $scope
    });
  };

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('authenticates with valid credentials', function () {
    $scope.credentials = {
      username: 'xoo',
      password: 'bar'
    };

    var authenticationCtrl = createController($scope);

    $httpBackend.expectPOST('/login', '{"username":"xoo","password":"bar"}').respond(200);

    $scope.login();

    $httpBackend.flush();

    expect($location.path()).toBe('/home');
  });

  it('goes to / when login fails', function () {
    var authenticationCtrl = createController($scope);

    $httpBackend.expectPOST('/login').respond(401);

    $scope.login();

    $httpBackend.flush();

    expect($location.path()).toBe('/');
  });
});
