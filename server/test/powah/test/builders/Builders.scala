package powah.test.builders

import scala.slick.session.Session

trait Builders {
  def anExercise(implicit s: Session) = new ExerciseBuilder
  def aRepSet(implicit s: Session) = new RepSetBuilder
  def aUser(implicit s: Session) = new UserBuilder
}
