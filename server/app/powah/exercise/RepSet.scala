package powah.exercise

import play.api.db.slick.Config.driver.simple._
import powah.user.Users
import org.joda.time.DateTime
import com.github.tototoshi.slick.PostgresJodaSupport._

case class RepSet(
  id: Long,
  weight: Float,
  reps: Int,
  time: DateTime,
  userId: Long,
  exerciseId: Long
)

case class RepSetOutput(
  id: Long,
  weight: Float,
  reps: Int
)

case class RepSetInput(weight: Float, reps: Int)

class RepSetTable(tag: Tag) extends Table[RepSet](tag, "rep_set") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def weight = column[Float]("weight")
  def reps = column[Int]("reps")
  def time = column[DateTime]("time")
  def userId = column[Long]("user_id")
  def exerciseId = column[Long]("exercise_id")

  def * = (id, weight, reps, time, userId, exerciseId) <> (RepSet.tupled, RepSet.unapply)

  def user = foreignKey("user_fk", userId, Users)(_.id)
  def exercise = foreignKey("exercise_fk", exerciseId, Exercises)(_.id)
}

object RepSets extends TableQuery(new RepSetTable(_)) {
  def forInsert = {
    RepSets.map(rs => (rs.weight, rs.reps, rs.time, rs.userId, rs.exerciseId)) returning RepSets.map(_.id)
  }
}
