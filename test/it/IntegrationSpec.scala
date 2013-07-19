package it

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import testhelpers.ItBase

class IntegrationSpec extends ItBase {
  "The application" should {
    "show the title on its home page" in itEnv {
      browser.goTo(homePage)

      browser.pageSource must contain("A list of entries")
    }
    
    "allow adding entries" in itEnv {
      browser.goTo(homePage)
      
      browser.fill("input[type=\"text\"]").`with`("trololoo")
      browser.click("input[type=\"submit\"]")
      browser.waitUntil {
        browser.findFirst("li").getText().trim() must equalTo ("trololoo")
      }
    }
  }
}
