package repositories

import com.google.inject.Inject
import models.{Entry, Entries}
import services.DbService
import services.DbService.driver._

class EntryRepository @Inject()(val db: DbService) {
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
