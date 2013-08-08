package repositories

import com.google.inject.Inject
import models.{Users, User}
import services.DbService
import services.DbService.driver._

class UserRepository @Inject()(val db: DbService) {
  def findByUsername(username: String): Option[User] = {
    db.withSession { implicit session =>
      val q = for { u <- Users if u.username === username } yield u

      q.firstOption
    }
  }

  def existsWithUsernameAndPassword(username: String, password: String): Boolean = {
    db.withSession { implicit session =>
      val q = Users
        .filter(u => u.username === username && u.password === password)
        .take(1)

      q.firstOption match {
        case Some(user) => true
        case None => false
      }
    }
  }
}
