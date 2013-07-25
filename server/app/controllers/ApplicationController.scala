package controllers

import play.api.mvc._
import java.io.File

class ApplicationController() extends Controller {
  def html(file: String) = Action {
    val f = new File(file)

    if (f.exists())
      Ok(scala.io.Source.fromFile(f.getCanonicalPath).mkString).as("text/html")
    else NotFound
  }
}
