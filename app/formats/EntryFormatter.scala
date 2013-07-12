package formats

import models.Entry
import play.api.libs.json._

object EntryFormatter {
  implicit object EntryFormat extends Format[Entry] {
    def reads(json: JsValue) = JsSuccess(Entry(
      (json \ "id").asOpt[Int],
      (json \ "name").as[String]
    ))

    def writes(entry: Entry) = JsObject(Seq(
      "id" -> JsNumber(entry.id.get),
      "name" -> JsString(entry.name)
    ))
  }
}
