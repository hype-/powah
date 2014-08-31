(function () {
  'use strict';

  var app = angular.module('powah');

  app.factory('RepSetService', ['$rootScope', '$http', function ($rootScope, $http) {
    $rootScope.exercises = [];

    return {
      getRepSets: function (date) {
        $http.get('/days/' + date + '/exercises').then(function (response) {
          response.data.exercises.forEach(function (exercise) {
            var repSets = exercise.rep_sets.map(function (repSet) {
              return {
                id: repSet.id,
                weight: repSet.weight,
                reps: repSet.reps
              };
            });

            $rootScope.exercises.push({
              id: exercise.id,
              name: exercise.name,
              repSets: repSets
            });
          });
        });
      },

      addRepSet: function (newRepSet, date) {
        var newExercise = {};
        var data = angular.toJson({name: newRepSet.exerciseName});

        $http.post('/exercises', data).then(function (response) {
          var weight = parseFloat(newRepSet.weight);
          var reps = parseInt(newRepSet.reps, 10);

          newExercise.name = newRepSet.exerciseName;
          newExercise.id = response.data.id;

          var url = '/days/' + date + '/exercises/' + response.data.id + '/rep_sets';
          var data = angular.toJson({
            weight: weight,
            reps: reps
          });

          $http.post(url, data).then(function (repSetResponse) {
            newExercise.repSets = [{
              id: repSetResponse.data.id,
              weight: weight,
              reps: reps
            }];
          });
        })
        .then(function () {
          $rootScope.exercises.push(newExercise);
        });
      }
    };
  }]);
})();
