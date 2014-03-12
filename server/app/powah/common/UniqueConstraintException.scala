package powah.common

import scala.util.Failure

class UniqueConstraintException(msg: String) extends RuntimeException(msg)

object UniqueConstraintException {
  def convertFrom(e: Exception): Exception = {
    if (e.getMessage.startsWith("ERROR: duplicate key")) {
      new UniqueConstraintException(e.getMessage)
    } else {
      e
    }
  }
}
