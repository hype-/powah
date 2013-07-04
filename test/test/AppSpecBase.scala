package test

import org.specs2.mutable._
import org.specs2.execute.AsResult
import play.api.Play.current

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
      case None => throw new IllegalStateException("No DB session. Did you forget to use withTestApp?")
    }
  }
  
  protected def withTestApp[T](block: => T): T = {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      play.api.db.slick.DB.withSession { s =>
        _session = Some(s)
        try {
          block
        } finally {
          _session = None
        }
      }
    }
  }
}
