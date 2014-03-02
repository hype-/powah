package powah.exercisemodule

import play.api.libs.json._
import play.api.libs.functional.syntax._
import powah.exercise.{RepSetOutput, ExerciseWithRepSetsOutput, ExerciseOutput, ExerciseInput}
import RepSetFormatter._

object ExerciseFormatter {
  implicit val exerciseInputReads = (__ \ "name").read[String].map(ExerciseInput(_))

  implicit val exerciseOutputWrites = (
    (__ \ "id").write[Long] and
    (__ \ "name").write[String]
  )(unlift(ExerciseOutput.unapply))

  implicit val exerciseWithRepSetsOutputWrites = (
    (__ \ "id").write[Long] and
    (__ \ "name").write[String] and
    (__ \ "rep_sets").write(Writes.traversableWrites[RepSetOutput](repSetOutputWrites))
  )(unlift(ExerciseWithRepSetsOutput.unapply))
}
