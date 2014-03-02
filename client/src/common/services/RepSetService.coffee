"use strict"

app = angular.module('powah')

app.factory(
  'RepSetService',
  [
    '$rootScope', '$http', ($rootScope, $http) ->
      $rootScope.exercises = []

      {
        getRepSets: (date) ->
          $http.get("/days/#{date}/exercises").then (response) ->
            response.data.exercises.forEach (exercise) ->
              repSets = exercise.rep_sets.map (repSet) ->
                {
                  id: repSet.id,
                  weight: repSet.weight,
                  reps: repSet.reps
                }

              $rootScope.exercises.push({
                id: exercise.id,
                name: exercise.name,
                repSets: repSets
              })

        addRepSet: (newRepSet, date) ->
          newExercise = {}

          $http.post('/exercises', angular.toJson({name: newRepSet.exerciseName}))
            .then (response) ->
              weight = parseFloat(newRepSet.weight)
              reps = parseInt(newRepSet.reps, 10)

              newExercise.name = newRepSet.exerciseName
              newExercise.id = response.data.id

              $http.post("/days/#{date}/exercises/#{response.data.id}/rep_sets", angular.toJson({
                weight: weight,
                reps: reps
              }))
                .then (repSetResponse) ->
                  newExercise.repSets = [{
                    id: repSetResponse.data.id,
                    weight: weight,
                    reps: reps
                  }]

            .then ->
              $rootScope.exercises.push(newExercise)
      }
  ]
)
