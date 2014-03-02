package powah.test.functional

import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import powah.test.helpers.{TestUser, AppSpecBase}

class AuthenticationSpec extends AppSpecBase {
  "Authentication" should {
    "login an user with valid credentials" in testApp {
      createTestUser

      val loginResult = route(FakeRequest(POST, "/login")
        .withHeaders(CONTENT_TYPE -> "application/json")
        .withBody(
          Json.parse("""{"username": "%s", "password": "%s"}""".format(
            TestUser.username,
            TestUser.password
          ))
        )
      ).get

      assertJsonResponse(loginResult, OK)

      Helpers.session(loginResult).get("username") must beSome(TestUser.username)
    }

    "error with invalid credentials" in testApp {
      createTestUser

      val loginResult = route(FakeRequest(POST, "/login")
        .withHeaders(CONTENT_TYPE -> "application/json")
        .withBody(
          Json.parse("""{"username": "%s", "password": "%s"}""".format(
            TestUser.username,
            "invalid password"
          ))
        )
      ).get

      assertJsonResponse(loginResult, UNAUTHORIZED)

      Helpers.session(loginResult).get("username") must beNone
    }

    "redirect to login page without authentication" in testApp {
      val result = route(FakeRequest(GET, "/exercises")).get

      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/")
    }
  }
}
