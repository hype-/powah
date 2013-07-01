package controllers

import play.api.mvc._
import play.api.libs.json._
import com.google.inject.Inject
import services.EntryService

class Application @Inject()(entryService: EntryService) extends Controller {
  def index = Action {
    Ok(views.html.index(entryService.getEntries))
  }

  def addEntry = Action(parse.json) { request =>
    (request.body \ "name").asOpt[String].map { name =>
      val entry = entryService.addEntry(name)

      Created(Json.toJson("lol"))
        .withHeaders(LOCATION -> "/entries/%d".format(entry.id.get))
    }.getOrElse {
      BadRequest
    }
  }
}
