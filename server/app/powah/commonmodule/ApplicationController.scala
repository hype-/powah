package powah.commonmodule

import play.api.mvc._
import java.io.File
import com.typesafe.config.ConfigFactory

class ApplicationController() extends Controller {
  def index = html(ConfigFactory.load.getString("client_index"))

  def html(file: String) = Action {
    val f = new File(file)

    if (f.exists())
      Ok(scala.io.Source.fromFile(f.getCanonicalPath).mkString).as("text/html")
    else NotFound
  }
}
