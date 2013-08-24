package powah.user

import com.google.inject.Inject
import powah.common.DbService
import DbService.driver._

class UserRepository @Inject()(val db: DbService) {
  def findByUsername(username: String): Option[User] = {
    db.withSession { implicit session =>
      val q = for { u <- Users if u.username === username } yield u

      q.firstOption
    }
  }
}
