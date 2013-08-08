package powah.user

import play.api.libs.json._
import play.api.libs.functional.syntax._

object UsernamePasswordInputDeserializer {
  implicit val usernamePasswordInputReads = (
    (__ \ "username").read[String] and
    (__ \ "password").read[String]
  )(UsernamePasswordInput)
}
