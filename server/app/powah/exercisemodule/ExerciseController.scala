package powah.exercisemodule

import com.google.inject.Inject
import play.api.libs.json._
import play.api.mvc._
import powah.commonmodule.Secured
import powah.exercise._
import ExerciseFormatter._
import scala.util.{Success, Failure}

class ExerciseController @Inject()(exerciseService: ExerciseService)
  extends Controller with Secured {

  def addExercise = AuthenticatedAction(parse.json) { username => implicit request =>
    request.body match {
      case json: JsValue =>
        json.validate[ExerciseInput](exerciseInputReads).map { input =>
          val exercise = exerciseService.add(input).get

          Created(Json.toJson(ExerciseOutput(exercise.id, exercise.name)))
            .withHeaders(
              CONTENT_TYPE -> JSON,
              LOCATION -> "/exercises/%d".format(exercise.id)
            )
        }.recoverTotal {
          e => BadRequest(JsError.toFlatJson(e)).withHeaders(CONTENT_TYPE -> "application/json")
        }
    }
  }

  def getExercises(name: Option[String]) = AuthenticatedAction { username => request =>
    val exercises = name match {
      case Some(n) => exerciseService.searchByName(n)
      case None => exerciseService.getAll
    }

    val exerciseOutputs = exercises.map(e => ExerciseOutput(e.id, e.name))

    Ok(Json.toJson(Map("exercises" -> exerciseOutputs.map(Json.toJson(_)))))
  }
}
