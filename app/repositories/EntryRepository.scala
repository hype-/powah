package repositories

import com.google.inject.Inject
import models.{Entry, Entries}
import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._

class EntryRepository @Inject()(val db: DB) {
  def findAll: List[Entry] = {
    db.withSession { implicit session =>
      Query(Entries).list
    }
  }

  def addEntry(name: String): Entry = {
    db.withSession { implicit session =>
      val entry = Entry(None, name)
      val newId = Entries.autoInc.insert(entry)
      entry.copy(id = Some(newId))
    }
  }
}
