package test

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.test.FakeHeaders
import play.api.mvc.Result
import play.api.db.slick.Config.driver.simple._
import models.{Entry, Entries}
import formats.EntryFormatter._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends AppSpecBase {
  "Application" should {
    "send 404 on a bad request" in testApp {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "show front page" in testApp {
      val result = route(FakeRequest(GET, "/")).get

      status(result) must equalTo(OK)
      contentType(result) must beSome.which(_ === "text/html")
      contentAsString(result) must contain ("A list of entries")
    }

    "get entries" in testApp {
      Entries.forInsert.insert("xoo entry")
      val entry = Query(Entries).first

      val result = route(FakeRequest(
        GET,
        "/entries"
      ).withHeaders(CONTENT_TYPE -> "application/json")).get

      status(result) must equalTo(OK)
      contentType(result) must beSome.which(_ === "application/json")

      val resultEntry = (Json.parse(contentAsString(result)) \ "data")(0).as[Entry]

      resultEntry must equalTo(entry)
    }

    "add an entry" in testApp {
      val Some(result) = route(FakeRequest(
        POST,
        "/entries",
        FakeHeaders(Seq(CONTENT_TYPE -> Seq("application/json"))),
        Json.parse("""{"name": "some name"}""")
      ))

      assertJsonResponse(result, CREATED)

      val entry = assertEntryWasCreated()

      header(LOCATION, result) must beSome
        .which(_ === "/entries/%d".format(entry.id.get)) // TODO: Absolute URL
    }

    def assertJsonResponse(result: Result, responseCode: Int) {
      contentType(result) must beSome.which(_ === "application/json")
      status(result) must equalTo(CREATED)
    }

    def assertEntryWasCreated(): Entry = {
      val entry = Query(Entries).first

      entry.name must equalTo("some name")

      entry
    }
  }
}
