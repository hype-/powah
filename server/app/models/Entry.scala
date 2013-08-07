package models

import play.api.db.slick.Config.driver.simple._

case class EntryOutput(id: Long, name: String)

case class EntryInput(name: String, userId: Long)

case class Entry(id: Long, name: String, userId: Long) {
  def toOutput = EntryOutput(id, name)
}

object Entries extends Table[Entry]("entry") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def userId = column[Long]("user_id")

  def * = id ~ name ~ userId <> (Entry, Entry.unapply _)

  def user = foreignKey("user_fk", userId, Users)(_.id)

  def forInsert = name ~ userId <> (EntryInput, EntryInput.unapply _) returning id
}
