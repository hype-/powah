package powah.user

import play.api.db.slick.Config.driver.simple._

case class User(id: Long, username: String, password: String)

class UserTable(tag: Tag) extends Table[User](tag, "user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def password = column[String]("password")

  def * = (id, username, password) <> (User.tupled, User.unapply)

  def uniqUsername = index("uniq_username", username, unique = true)
}

object Users extends TableQuery(new UserTable(_)) {
  def forInsert = Users.map(u => (u.username, u.password)) returning Users.map(_.id)
}
