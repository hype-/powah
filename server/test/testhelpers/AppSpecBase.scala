package testhelpers

import org.specs2.mutable._
import play.api.Play.current
import models.Entries
import com.typesafe.config.ConfigFactory
import play.api.db.slick.Config.driver.simple.Session
import play.api.db.slick.Config.driver.simple.ddlToDDLInvoker
import play.api.test.FakeApplication
import play.api.test.Helpers.running

trait AppSpecBase extends Specification {
  
  // Importing here will benefit subclasses too
  import play.api.test._
  import play.api.test.Helpers._
  import play.api.db.slick.Config.driver.simple._
  
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
}
