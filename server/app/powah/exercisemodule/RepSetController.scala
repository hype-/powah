package powah.exercisemodule

import play.api.mvc._
import play.api.libs.json._
import com.google.inject.Inject
import powah.exercise._
import RepSetFormatter._
import ExerciseFormatter._
import powah.user.UserService
import powah.commonmodule.Secured
import org.joda.time.DateTime

class RepSetController @Inject()(
  repSetService: RepSetService,
  userService: UserService,
  exerciseService: ExerciseService
) extends Controller with Secured {

  def addRepSet(date: String, exerciseId: Long) = {
    AuthenticatedAction(parse.json) { username => implicit request =>
      request.body match {
        case json: JsValue =>
          json.validate[RepSetInput](repSetInputReads).map { input =>
            val repSet = repSetService.add(
              input,
              new DateTime(date),
              userService.getByUsername(username).get,
              exerciseService.getById(exerciseId).get // TODO: Check if exists
            )

            Created.withHeaders(
              CONTENT_TYPE -> JSON,
              LOCATION -> "/days/%s/exercises/%d/rep_sets/%d".format(
                date,
                exerciseId,
                repSet.id
              )
            )
          }.recoverTotal {
            e => BadRequest(JsError.toFlatJson(e)).withHeaders(CONTENT_TYPE -> "application/json")
          }

        case _ => BadRequest // TODO: test
      }
    }
  }

  def getRepSets(date: String) = AuthenticatedAction { username => request =>
    val user = userService.getByUsername(username).get

    val exercises = exerciseService.getWithRepSetsForDate(new DateTime(date), user).map {
      exercise => ExerciseWithRepSetsOutput(
        exercise.id,
        exercise.name,
        exercise.repSets.map { repSet => RepSetOutput(
          repSet.id,
          repSet.weight,
          repSet.reps
        )}
      )
    }

    Ok(Json.toJson(Map("exercises" -> exercises.map(Json.toJson(_)))))
  }
}
