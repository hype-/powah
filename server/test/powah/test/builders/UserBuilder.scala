package powah.test.builders

import powah.user.{Users, User}
import scala.slick.session.Session

case class UserBuilder(
  username: Option[String] = None,
  password: String = "password"
)(implicit s: Session) {
  def withUsername(username: String) = copy(username = Some(username))
  def withPassword(password: String) = copy(password = password)

  def build: User = {
    val username = this.username.getOrElse("username-%d".format(UserBuilder.nextInt))

    val id = Users.forInsert.insert(
      username,
      password
    )

    User(id, username, password)
  }
}

object UserBuilder {
  private var instances = 0

  def nextInt = {
    instances = instances + 1
    instances
  }
}
