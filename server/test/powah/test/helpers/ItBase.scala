package powah.test.helpers

trait ItBase extends AppSpecBase with ApplicationUrls {

  import play.api.test.Helpers._
  import play.api.test._

  protected val webdriver = FIREFOX

  private var _browser = Option.empty[TestBrowser]

  protected def browser: TestBrowser = {
    _browser match {
      case Some(b) => b
      case None => throw new IllegalStateException("No browser. Did you forget to use itEnv?")
    }
  }

  protected def itEnv[T](block: => T): T = {
    val testServer = TestServer(testServerPort, FakeApplication(additionalConfiguration = testDatabaseConfig))
    running(testServer, webdriver) { browser =>
      withTestDb {
        _browser = Some(browser)
        try {
          val result = block
          browser.goTo(homePage)  // To avoid any AJAX requests going out while we're switching to the next test
          result
        } finally {
          _browser = None
        }
      }
    }
  }
}
