package powah.exercise

import com.google.inject.Inject
import org.joda.time.DateTime
import powah.user.User
import scala.util.Try

class ExerciseService @Inject()(exerciseRepository: ExerciseRepository) {
  def getById(id: Long): Option[Exercise] = exerciseRepository.find(id)

  def add(input: ExerciseInput): Try[Exercise] = exerciseRepository.add(input)

  def getAll: Seq[Exercise] = exerciseRepository.findAll

  def getWithRepSetsForDate(date: DateTime, user: User): Seq[ExerciseWithRepSets] = {
    exerciseRepository.findWithRepSetsForDate(date, user)
  }
}
