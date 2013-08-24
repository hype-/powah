package powah.usermodule

import play.api.mvc._
import play.api.libs.json._
import com.google.inject.Inject
import powah.user._
import powah.user.UsernamePasswordInputDeserializer._
import powah.commonmodule.Secured

class AuthenticationController @Inject()(
  authenticationService: AuthenticationService
) extends Controller with Secured {

  def login = Action(parse.json) { request =>
    request.body.validate[UsernamePasswordInput].map {
      case input: UsernamePasswordInput => {
        if (authenticationService.authenticate(input)) {
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
