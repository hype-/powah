package powah.test.helpers

import com.typesafe.config.ConfigFactory
import play.api.db.slick.Config.driver.simple._
import powah.exercise.{Exercises, RepSets}
import powah.user.Users

trait TestDb {
  private var isInitialized = false

  def clearBeforeTest(s: Session) {
    if (isInitialized) {
      truncateAllTables(s)
    } else {
      dropAllTables(s)
      createAllTables(s)
      isInitialized = true
    }
  }

  def createAllTables(s: Session) = {
    allTables.foreach(_.ddl.create(s))
  }

  def dropAllTables(s: Session) = {
    // We need to eventually worry about the correct drop order or temporarily disable FK constraints.
    for (table <- allTables.reverse) {
      s.conn.createStatement().execute(
        "DROP TABLE IF EXISTS \"" + table.baseTableRow.tableName + "\""
      )
    }
  }

  def truncateAllTables(s: Session) = {
    for (table <- allTables) {
      s.conn.createStatement().execute(
        "TRUNCATE \"" + table.baseTableRow.tableName + "\" CASCADE"
      )
    }
  }

  def allTables = Seq(
    Exercises,
    Users,
    RepSets
  )
}

object TestDb extends TestDb {
  val url = ConfigFactory.load.getString("test_database.url")
}
