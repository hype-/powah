describe('exercise controller', function() {
  var $controller, $httpBackend, $routeParams, $scope, createController;

  $routeParams = {};

  beforeEach(module('powah'));

  beforeEach(inject(function($injector) {
    $controller = $injector.get('$controller');
    $scope = $injector.get('$rootScope');
    $httpBackend = $injector.get('$httpBackend');
  }));

  createController = function ($scope, $routeParams) {
    return $controller('ExerciseCtrl', {
      $scope: $scope,
      $routeParams: $routeParams
    });
  };

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('adds a new exercise with rep set', function () {
    var exerciseCtrl;

    $scope.newRepSet = {
      exerciseName: 'foobar',
      weight: "100.5",
      reps: "6"
    };

    $routeParams.date = '2013-01-12';

    $httpBackend.whenGET('/days/' + $routeParams.date + '/exercises').respond(200, angular.toJson({
      exercises: []
    }));

    exerciseCtrl = createController($scope, $routeParams);

    $httpBackend.expectPOST('/exercises', '{"name":"foobar"}').respond(201, '{"id":123}');
    $httpBackend.expectPOST(
      '/days/' + $routeParams.date + '/exercises/123/rep_sets',
      '{"weight":100.5,"reps":6}'
    ).respond(201, '{"id":456}');

    $scope.addRepSet();

    expect($scope.exercises.length).toBe(0);

    $httpBackend.flush();

    expect($scope.exercises.length).toBe(1);

    expect($scope.exercises[0]).toEqual({
      id: 123,
      name: 'foobar',
      repSets: [{
        id: 456,
        weight: 100.5,
        reps: 6
      }]
    });
  });

  it('shows exercises with rep sets by day', function () {
    var exerciseCtrl;

    var response = {
      exercises: [
        {
          id: 3,
          name: 'squat',
          rep_sets: [{
            id: 1,
            weight: 123.0,
            reps: 5
          }]
        },
        {
          id: 4,
          name: 'bench',
          rep_sets: [{
            id: 2,
            weight: 80.0,
            reps: 6
          }]
        }
      ]
    };

    $routeParams.date = '2014-02-16';

    $httpBackend.expectGET("/days/" + $routeParams.date + "/exercises").respond(200, angular.toJson(response));

    exerciseCtrl = createController($scope, $routeParams);

    $httpBackend.flush();

    expect($scope.exercises.length).toBe(2);
    expect($scope.exercises[0]).toEqual({
      id: 3,
      name: 'squat',
      repSets: [{
        id: 1,
        weight: 123.0,
        reps: 5
      }]
    });

    expect($scope.exercises[1]).toEqual({
      id: 4,
      name: 'bench',
      repSets: [{
        id: 2,
        weight: 80,
        reps: 6
      }]
    });
  });
});
