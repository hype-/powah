package powah.exercise

import play.api.db.slick.Config.driver.simple._

case class Exercise(id: Long, name: String)

case class ExerciseInput(name: String)

case class ExerciseOutput(id: Long, name: String)

case class ExerciseWithRepSetsOutput(id: Long, name: String, repSets: Seq[RepSetOutput])

case class ExerciseWithRepSets(id: Long, name: String, repSets: Seq[RepSet])

object Exercises extends Table[Exercise]("exercise") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  def * = id ~ name <> (Exercise, Exercise.unapply _)

  def uniqName = index("uniq_name", name, unique = true)

  def forInsert = name returning id
}
