package powah.test.builders

import powah.exercise.{Exercises, Exercise}
import scala.slick.jdbc.JdbcBackend.Session

case class ExerciseBuilder(name: Option[String] = None)(implicit s: Session) {
  def withName(name: String): ExerciseBuilder = copy(name = Some(name))

  def build: Exercise = {
    val name = this.name.getOrElse("exercise-%d".format(ExerciseBuilder.nextInt))

    val id = Exercises.forInsert.insert(name)

    Exercise(id, name)
  }
}

object ExerciseBuilder {
  private var instances = 0

  def nextInt = {
    instances = instances + 1
    instances
  }
}
