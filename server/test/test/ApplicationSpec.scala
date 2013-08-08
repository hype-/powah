package test

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.db.slick.Config.driver.simple._
import testhelpers.AppSpecBase
import powah.user.Users
import powah.entry.{Entries, EntryInput, Entry}

class ApplicationSpec extends AppSpecBase {
  val TEST_USER_USERNAME = "test@example.com"
  val TEST_USER_PASSWORD = "password"
  val TEST_USER_IN_SESSION = "username" -> TEST_USER_USERNAME

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

    "login an user with valid credentials" in testApp {
      createTestUser

      val loginResult = route(FakeRequest(POST, "/login")
        .withHeaders(CONTENT_TYPE -> "application/json")
        .withBody(
          Json.parse("""{"username": "%s", "password": "%s"}""".format(
            TEST_USER_USERNAME,
            TEST_USER_PASSWORD
          ))
        )
      ).get

      assertJsonResponse(loginResult, OK)

      Helpers.session(loginResult).get("username") must beSome(TEST_USER_USERNAME)
    }

    "error with invalid credentials" in testApp {
      createTestUser

      val loginResult = route(FakeRequest(POST, "/login")
        .withHeaders(CONTENT_TYPE -> "application/json")
        .withBody(
          Json.parse("""{"username": "%s", "password": "%s"}""".format(
            TEST_USER_USERNAME,
            "invalid password"
          ))
        )
      ).get

      assertJsonResponse(loginResult, UNAUTHORIZED)

      Helpers.session(loginResult).get("username") must beNone
    }

    "redirect to login page without authentication" in testApp {
      val result = route(FakeRequest(GET, "/entries")).get

      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/")
    }

    "get entries" in testApp {
      val userId = createTestUser
      Entries.forInsert.insert(EntryInput("xoo entry", userId))
      val entry = Query(Entries).first

      val result = route(FakeRequest(GET, "/entries")
        .withSession(TEST_USER_IN_SESSION)
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
      ).toString

      contentAsString(result) must equalTo(expectedJson)
    }

    def createTestUser: Long =
      Users.forInsert.insert(TEST_USER_USERNAME, TEST_USER_PASSWORD)

    "add an entry" in testApp {
      createTestUser

      val result = route(FakeRequest(POST, "/entries")
        .withHeaders(CONTENT_TYPE -> "application/json")
        .withSession(TEST_USER_IN_SESSION)
        .withBody(Json.parse("""{"name": "some name"}"""))
      ).get

      assertJsonResponse(result, CREATED)

      val entry = assertEntryWasCreated()

      // TODO: Absolute URL?
      header(LOCATION, result) must beSome("/entries/%d".format(entry.id))
    }

    def assertJsonResponse(result: Result, responseCode: Int) {
      contentType(result) must beSome("application/json")

      status(result) must equalTo(responseCode).setMessage(
        "'%d' is not equal to '%d'\nContent: %s".format(
          status(result),
          responseCode,
          contentAsString(result)
        )
      )
    }

    def assertEntryWasCreated(): Entry = {
      val entriesWithUsers = for {
        e <- Entries
        u <- Users if e.userId === u.id
      } yield (e, u)

      val (entry, user) = entriesWithUsers.first

      entry.name must equalTo("some name")
      user.username must equalTo(TEST_USER_USERNAME)

      entry
    }
  }
}
