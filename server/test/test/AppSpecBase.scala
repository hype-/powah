package test

import org.specs2.mutable._
import play.api.Play.current
import models.Entries
import com.typesafe.config.ConfigFactory

trait AppSpecBase extends Specification {
  self: Specification =>

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
    if (_session.isDefined) {
      block  // Permit nesting testApp calls, though I'm not sure why one would want to.
    } else {
      running(FakeApplication(additionalConfiguration = testDatabase)) {
        play.api.db.slick.DB.withSession { s =>
          _session = Some(s)
          try {
            createTables(s)
            block
          } finally {
            dropTables(s)
            _session = None
          }
        }
      }
    }
  }

  private def testDatabase = {
    Map(
      "db.default.url" -> ConfigFactory.load.getString("db.default.url")
    )
  }

  private def createTables(s: Session) = {
    Entries.ddl.create(s)
  }

  private def dropTables(s: Session) = {
    Entries.ddl.drop(s)
  }
}
