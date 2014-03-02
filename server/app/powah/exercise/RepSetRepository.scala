package powah.exercise

import com.google.inject.Inject
import powah.common.DbService
import DbService.driver._
import powah.user.User
import org.joda.time.DateTime

class RepSetRepository @Inject()(val db: DbService) {
  def add(input: RepSetInput, time: DateTime, user: User, exercise: Exercise): RepSet = {
    db.withSession { implicit session =>
      val userId = user.id
      val id = RepSets.forInsert.insert(input.weight, input.reps, time, userId, exercise.id)

      RepSet(id, input.weight, input.reps, time, userId, exercise.id)
    }
  }
}
