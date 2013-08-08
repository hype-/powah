package powah.entry

import com.google.inject.Inject
import powah.common.DbService
import DbService.driver._
import powah.user.User

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
