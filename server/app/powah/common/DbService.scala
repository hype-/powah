package powah.common

import play.api.db.slick.DB
import scala.slick.jdbc.JdbcBackend.{Database, Session}

class DbService {
  private val currentApp = play.api.Play.current

  def openDatabase(): Database = DB("default")(currentApp)

  def withSession[A](f: (Session) => A): A = {
    openDatabase().withSession(f)
  }
}

object DbService {
  val driver = play.api.db.slick.Config.driver.simple
}
