package repositories

import com.google.inject.Inject
import models.{EntryInput, Entry, Entries, User}
import services.DbService
import services.DbService.driver._

class EntryRepository @Inject()(val db: DbService) {
  def findAll: List[Entry] = {
    db.withSession { implicit session =>
      Query(Entries).list
    }
  }

  def add(name: String, user: User): Entry = {
    db.withSession { implicit session =>
      val userId = user.id.get
      val id = Entries.forInsert.insert(EntryInput(name, userId))

      Entry(id, name, userId)
    }
  }
}
