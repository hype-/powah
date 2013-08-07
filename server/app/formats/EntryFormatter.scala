package formats

import models.EntryOutput
import play.api.libs.json._

object EntryFormatter {
  implicit object EntryFormat extends Format[EntryOutput] {
    def reads(json: JsValue) = JsSuccess(EntryOutput(
      (json \ "id").as[Long],
      (json \ "name").as[String]
    ))

    def writes(entry: EntryOutput) = JsObject(Seq(
      "id" -> JsNumber(entry.id),
      "name" -> JsString(entry.name)
    ))
  }
}
