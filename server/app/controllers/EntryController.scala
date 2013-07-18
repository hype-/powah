package controllers

import play.api.mvc._
import play.api.libs.json._
import com.google.inject.Inject
import services.EntryService
import formats.EntryFormatter._

class EntryController @Inject()(entryService: EntryService) extends Controller {
  def addEntry = Action(parse.json) { request =>
    (request.body \ "name").asOpt[String].map { name =>
      val entry = entryService.addEntry(name)

      Created(Json.toJson("lol"))
        .withHeaders(LOCATION -> "/entries/%d".format(entry.id.get))
    }.getOrElse {
      BadRequest
    }
  }

  def getEntries = Action {
    val entries = entryService.getEntries

    Ok(Json.toJson(Map("data" -> entries.map(entry => Json.toJson(entry)))))
  }
}
