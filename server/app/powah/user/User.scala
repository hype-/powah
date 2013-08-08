package powah.user

import play.api.db.slick.Config.driver.simple._

case class User(id: Option[Long], username: String, password: String)

object Users extends Table[User]("user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def password = column[String]("password")

  def * = id.? ~ username ~ password <> (User, User.unapply(_))

  def idxUsername = index("uniq_username", username, unique = true)

  def forInsert = username ~ password returning id
}
