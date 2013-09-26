package powah.test.helpers

import com.github.t3hnar.bcrypt._
import org.specs2.mutable._
import play.api.Play.current
import play.api.mvc.Result
import powah.user.Users

trait AppSpecBase extends Specification {
  // Importing here will benefit subclasses too

  import play.api.test.Helpers._
  import play.api.test._

  private var _session = Option.empty[scala.slick.session.Session]
  protected implicit def session = {
    _session match {
      case Some(s) => s
      case None => throw new IllegalStateException("No DB session. Did you forget to use testApp?")
    }
  }

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

  protected def assertJsonResponse(result: Result, responseCode: Int) {
    contentType(result) must beSome("application/json")

    status(result) must equalTo(responseCode).setMessage(
      "'%d' is not equal to '%d'\nContent: %s".format(
        status(result),
        responseCode,
        contentAsString(result)
      )
    )
  }

  protected def createTestUser: Long =
    Users.forInsert.insert(TestUser.username, TestUser.password.bcrypt)
}
