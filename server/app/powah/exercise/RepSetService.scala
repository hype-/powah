package powah.exercise

import com.google.inject.Inject
import powah.user.User
import org.joda.time.DateTime

class RepSetService @Inject()(repSetRepository: RepSetRepository) {
  def add(input: RepSetInput, time: DateTime, user: User, exercise: Exercise): RepSet = {
    repSetRepository.add(input, time, user, exercise)
  }
}
