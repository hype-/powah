package powah.test.functional

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.db.slick.Config.driver.simple._
import powah.test.helpers.{TestUser, AppSpecBase}
import powah.user.Users
import powah.entry.{Entries, EntryInput, Entry}

class ApplicationSpec extends AppSpecBase {
  "Application" should {
    "send 404 on a bad request" in testApp {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "show front page" in testApp {
      val result = route(FakeRequest(GET, "/")).get

      status(result) must equalTo(OK)
      contentType(result) must beSome("text/html")
      contentAsString(result) must contain ("Login")
    }

    "get entries" in testApp {
      val userId = createTestUser
      Entries.forInsert.insert(EntryInput("xoo entry", userId))
      val entry = Query(Entries).first

      val result = route(FakeRequest(GET, "/entries")
        .withSession(TestUser.inSession)
        .withHeaders(CONTENT_TYPE -> "application/json")
      ).get

      assertJsonResponse(result, OK)

      val expectedJson = Json.obj(
        "data" -> Json.arr(
          Json.obj(
            "id" -> entry.id,
            "name" -> "xoo entry"
          )
        )
      ).toString()

      contentAsString(result) must equalTo(expectedJson)
    }

    "add an entry" in testApp {
      createTestUser

      val result = route(FakeRequest(POST, "/entries")
        .withHeaders(CONTENT_TYPE -> "application/json")
        .withSession(TestUser.inSession)
        .withBody(Json.parse("""{"name": "some name"}"""))
      ).get

      assertJsonResponse(result, CREATED)

      val entry = assertEntryWasCreated()

      // TODO: Absolute URL?
      header(LOCATION, result) must beSome("/entries/%d".format(entry.id))
    }

    def assertEntryWasCreated(): Entry = {
      val entriesWithUsers = for {
        e <- Entries
        u <- Users if e.userId === u.id
      } yield (e, u)

      val (entry, user) = entriesWithUsers.first

      entry.name must equalTo("some name")
      user.username must equalTo(TestUser.username)

      entry
    }
  }
}
