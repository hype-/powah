package powah.common

import scala.slick.session.Database
import play.api.db.slick.DB
import com.google.inject.Inject
import scala.slick.session.Session

class DbService @Inject()(db: DB) {
  private val currentApp = play.api.Play.current

  def openDatabase(): Database = db.database("default")(currentApp)

  def withSession[A](f: (Session) => A): A = {
    openDatabase().withSession(f)
  }
}

object DbService {
  val driver = play.api.db.slick.Config.driver.simple
}
