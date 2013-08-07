package controllers

import play.api.mvc._
import play.api.libs.json._
import com.google.inject.Inject
import services.{UserService, EntryService}
import formats.EntryFormatter._

class EntryController @Inject()(
  entryService: EntryService,
  userService: UserService
) extends Controller with Secured {

  def addEntry = AuthenticatedAction(parse.json) { username => implicit request =>
    request.body match {
      case json: JsValue => {
        (json \ "name").asOpt[String].map { name =>
          val entry = entryService.addEntry(
            name,
            userService.getByUsername(username).get
          )

          Created.withHeaders(
            CONTENT_TYPE -> JSON,
            LOCATION -> "/entries/%d".format(entry.id)
          )
        }.getOrElse(BadRequest) // TODO: test
      }
      case _ => BadRequest // TODO: test
    }
  }

  def getEntries = AuthenticatedAction { username => request =>
    val entries = entryService.getEntries

    Ok(Json.toJson(Map("data" -> entries.map(entry => Json.toJson(entry)))))
  }
}
