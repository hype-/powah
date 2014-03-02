package powah.exercisemodule

import play.api.libs.functional.syntax._
import play.api.libs.json._
import powah.exercise.{RepSetOutput, RepSetInput}

object RepSetFormatter {
  implicit val repSetInputReads = (
    (__ \ "weight").read[Float] and
    (__ \ "reps").read[Int]
  )(RepSetInput)

  implicit val repSetOutputWrites = (
    (__ \ "id").write[Long] and
    (__ \ "weight").write[Float] and
    (__ \ "reps").write[Int]
  )(unlift(RepSetOutput.unapply))
}
