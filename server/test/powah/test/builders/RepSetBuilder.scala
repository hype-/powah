package powah.test.builders

import powah.exercise.{RepSets, RepSet}
import scala.slick.session.Session
import org.joda.time.DateTime

case class RepSetBuilder(
  reps: Int = 4,
  weight: Float = 50,
  time: DateTime = new DateTime(),
  userId: Option[Long] = None,
  exerciseId: Option[Long] = None
)(implicit s: Session) {
  def withWeight(weight: Float) = copy(weight = weight)

  def withReps(reps: Int) = copy(reps = reps)

  def withTime(time: DateTime) = copy(time = time)

  def withExerciseId(exerciseId: Long) = copy(exerciseId = Some(exerciseId))

  def withUserId(userId: Long) = copy(userId = Some(userId))

  def build: RepSet = {
    val userId = this.userId.getOrElse(UserBuilder().build.id)
    val exerciseId = this.exerciseId.getOrElse(ExerciseBuilder().build.id)

    val id = RepSets.forInsert.insert(weight, reps, time, userId, exerciseId)

    RepSet(id, weight, reps, time, userId, exerciseId)
  }
}
