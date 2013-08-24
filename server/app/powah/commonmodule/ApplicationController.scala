package powah.commonmodule

import play.api.mvc._
import java.io.File
import com.typesafe.config.ConfigFactory

trait Secured {
  /**
   * Retrieve the currently authenticated user username.
   */
  private def username(request: RequestHeader) = request.session.get("username")

  /**
   * Redirect to front page if the user is not authenticated.
   */
  private def onUnauthenticated(request: RequestHeader) =
    Results.Redirect(powah.commonmodule.routes.ApplicationController.index)

  /**
   * Action for authenticated users.
   */
  def AuthenticatedAction(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthenticated) { user =>
      Action(request => f(user)(request))
    }
  }

  /**
   * Authenticated action with body parser.
   */
  def AuthenticatedAction(b: BodyParser[Any] = BodyParsers.parse.anyContent)
                         (f: => String => Request[Any] => Result) = {
    Security.Authenticated(username, onUnauthenticated) { user =>
      Action(b)(request => f(user)(request))
    }
  }
}

class ApplicationController() extends Controller {
  def index = html(ConfigFactory.load.getString("client_index"))

  def html(file: String) = Action {
    val f = new File(file)

    if (f.exists())
      Ok(scala.io.Source.fromFile(f.getCanonicalPath).mkString).as("text/html")
    else NotFound
  }
}
