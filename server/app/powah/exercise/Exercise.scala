package powah.exercise

import play.api.db.slick.Config.driver.simple._

case class Exercise(id: Long, name: String)

case class ExerciseInput(name: String)

case class ExerciseOutput(id: Long, name: String)

case class ExerciseWithRepSetsOutput(id: Long, name: String, repSets: Seq[RepSetOutput])

case class ExerciseWithRepSets(id: Long, name: String, repSets: Seq[RepSet])

class ExerciseTable(tag: Tag) extends Table[Exercise](tag, "exercise") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  def * = (id, name) <> (Exercise.tupled, Exercise.unapply)

  def uniqName = index("uniq_name", name, unique = true)
}

object Exercises extends TableQuery(new ExerciseTable(_)) {
  def forInsert = Exercises.map(e => e.name) returning Exercises.map(_.id)
}
