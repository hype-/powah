package powah.common

import com.github.t3hnar.bcrypt._

class PasswordEncoder {
  def encode(password: String): String = password.bcrypt

  def verify(password: String, hashed: String): Boolean = {
    password.isBcrypted(hashed)
  }
}
