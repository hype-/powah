package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.test.FakeHeaders
import play.api.test.FakeApplication
import play.api.mvc.Result
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import models.{Entry, Entries}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  "Application" should {
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }

    "front page should list entries" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        play.api.db.slick.DB.withSession { implicit session =>
          Entries.insert(Entry(None, "xoo entry"))

          val home = route(FakeRequest(GET, "/")).get

          status(home) must equalTo(OK)
          contentType(home) must beSome.which(_ === "text/html")
          contentAsString(home) must contain ("xoo entry")
        }
      }
    }

    "add an entry" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        play.api.db.slick.DB.withSession { implicit session =>
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
      }
    }

    def assertJsonResponse(result: Result, responseCode: Int) {
      contentType(result) must beSome.which(_ === "application/json")
      status(result) must equalTo(CREATED)
    }

    def assertEntryWasCreated(): Entry = {
      play.api.db.slick.DB.withSession { implicit session =>
        val entry = Query(Entries).first

        entry.name must equalTo("some name")

        entry
      }
    }
  }
}
