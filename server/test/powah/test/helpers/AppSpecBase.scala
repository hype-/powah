package powah.test.helpers

import com.github.t3hnar.bcrypt._
import org.specs2.mutable._
import play.api.Play.current
import play.api.mvc.SimpleResult
import powah.user.User
import powah.test.builders.Builders
import scala.concurrent.Future

trait AppSpecBase extends Specification with Builders with Finders {
  // Importing here will benefit subclasses too

  import play.api.test.Helpers._
  import play.api.test._

  private var _session = Option.empty[scala.slick.jdbc.JdbcBackend.Session]
  protected implicit def session = {
    _session match {
      case Some(s) => s
      case None => throw new IllegalStateException("No DB session. Did you forget to use testApp?")
    }
  }

  def dbSession = session

  protected def testApp[T](block: => T): T = {
    running(FakeApplication(additionalConfiguration = testDatabaseConfig)) {
      withTestDb {
        block
      }
    }
  }

  protected def withTestDb[T](block: => T): T = {
    if (_session.isDefined) {
      block  // Permit nesting testApp calls, though I'm not sure why one would want to.
    } else {
      play.api.db.slick.DB.withSession { s =>
        _session = Some(s)
        try {
          TestDb.clearBeforeTest(s)
          block
        } finally {
          _session = None
        }
      }
    }
  }

  protected def testDatabaseConfig = {
    Map(
      "db.default.url" -> TestDb.url
    )
  }

  protected def assertJsonResponse(result: Future[SimpleResult], responseCode: Int) {
    def contentAppended(text: String): String =
      text + "\n%s".format(contentAsString(result))

    contentType(result) must beSome("application/json").updateMessage(contentAppended)

    status(result) must equalTo(responseCode).updateMessage(contentAppended)
  }

  protected def createTestUser: User = {
    val username = TestUser.username
    val password = TestUser.password.bcrypt

    aUser.withUsername(username).withPassword(password).build
  }
}
