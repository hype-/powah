package powah.test.functional

import play.api.test._
import play.api.test.Helpers._
import powah.test.helpers.AppSpecBase

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
  }
}
