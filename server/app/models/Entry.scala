package models

import play.api.db.slick.Config.driver.simple._

case class Entry(id: Option[Long] = None, name: String)

object Entries extends Table[Entry]("entry") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  def * = id.? ~ name <> (Entry, Entry.unapply(_))

  def forInsert = name returning id
}
