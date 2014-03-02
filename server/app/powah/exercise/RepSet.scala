package powah.exercise

import play.api.db.slick.Config.driver.simple._
import powah.user.Users
import org.joda.time.DateTime
import com.github.tototoshi.slick.JodaSupport._

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

object RepSets extends Table[RepSet]("rep_set") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def weight = column[Float]("weight")
  def reps = column[Int]("reps")
  def time = column[DateTime]("time")
  def userId = column[Long]("user_id")
  def exerciseId = column[Long]("exercise_id")

  def * = id ~ weight ~ reps ~ time ~ userId ~ exerciseId <> (RepSet, RepSet.unapply _)

  def user = foreignKey("user_fk", userId, Users)(_.id)
  def exercise = foreignKey("exercise_fk", exerciseId, Exercises)(_.id)

  def forInsert = weight ~ reps ~ time ~ userId ~ exerciseId returning id
}
