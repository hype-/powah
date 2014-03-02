package powah.test.helpers

import powah.exercise.{Exercises, Exercise}
import powah.common.DbService.driver._

trait Finders {
  implicit def dbSession: Session

  def findExercise(id: Long): Exercise = {
    dbSession.withTransaction {
      val q = for { e <- Exercises if e.id === id } yield e

      q.first
    }
  }
}
