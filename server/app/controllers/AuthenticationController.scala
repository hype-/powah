package controllers

import play.api.mvc._
import play.api.libs.json._
import com.google.inject.Inject
import services.UserService
import models.UsernamePasswordInput
import formats.UsernamePasswordInputDeserializer._

class AuthenticationController @Inject()(
  userService: UserService
) extends Controller with Secured {

  def login = Action(parse.json) { request =>
    request.body.validate[UsernamePasswordInput].map {
      case input: UsernamePasswordInput => {
        if (userService.authenticate(input)) {
          Ok
            .withHeaders(CONTENT_TYPE -> JSON)
            .withSession("username" -> input.username)
        } else {
          Unauthorized.withHeaders(CONTENT_TYPE -> JSON)
        }
      }
    }.recoverTotal {
      e => BadRequest(JsError.toFlatJson(e)) // TODO: test
    }
  }
}
