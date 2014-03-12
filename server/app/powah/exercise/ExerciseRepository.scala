package powah.exercise

import com.google.inject.Inject
import powah.common.{UniqueConstraintException, DbService}
import DbService.driver._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import powah.user.User
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import scala.util.{Failure, Try}
import org.postgresql.util.PSQLException

class ExerciseRepository @Inject()(val db: DbService) {
  def find(id: Long): Option[Exercise] = {
    db.withSession { implicit session =>
      val q = for { e <- Exercises if e.id === id } yield e

      q.firstOption
    }
  }

  def findAll: Seq[Exercise] = {
    db.withSession { implicit session =>
      Query(Exercises).list
    }
  }

  def add(input: ExerciseInput): Try[Exercise] = {
    db.withSession { implicit session =>
      val name = input.name

      Try(Exercises.forInsert.insert(name))
        .map(id => Exercise(id, name))
        .recoverWith {
          case e: PSQLException =>
            Failure(UniqueConstraintException.convertFrom(e))
        }
    }
  }

  def findWithRepSetsForDate(date: DateTime, user: User): Seq[ExerciseWithRepSets] = {
    db.withSession { implicit session =>
      case class Result(
        exerciseId: Long,
        exerciseName: String,
        repSetId: Long,
        repSetWeight: Float,
        repSetReps: Int,
        repSetTime: DateTime,
        repSetUserId: Long
      )

      implicit val getResult = GetResult(r => Result(
        r.<<,
        r.<<,
        r.<<,
        r.<<,
        r.<<,
        DateTime.parse(r.<<, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")),
        r.<<
      ))

      val results = Q.query[(String, Long), Result]("""
        SELECT e.id, e.name, rs.id, rs.weight, rs.reps, rs.time, rs.user_id FROM exercise e
        INNER JOIN rep_set rs ON e.id = rs.exercise_id
        WHERE TO_CHAR(rs.time, 'YYYY-MM-DD')::DATE = ?::DATE
        AND rs.user_id = ?
      """).list(date.toString("yyyy-MM-dd"), user.id)

      val exercises = results.map(r => Exercise(r.exerciseId, r.exerciseName)).distinct
      val repSets = results.map(r => RepSet(
        r.repSetId,
        r.repSetWeight,
        r.repSetReps,
        r.repSetTime,
        r.repSetUserId,
        r.exerciseId
      )).groupBy(_.exerciseId)

      exercises.map(e => ExerciseWithRepSets(e.id, e.name, repSets(e.id)))
    }
  }
}
