package powah.user

import com.google.inject.Inject
import powah.common.PasswordEncoder

class UserService @Inject()(
  userRepository: UserRepository,
  passwordEncoder: PasswordEncoder
) {
  def getByUsername(username: String): Option[User] =
    userRepository.findByUsername(username)

  def authenticate(input: UsernamePasswordInput): Boolean =
    userRepository.findByUsername(input.username) match {
      case Some(user) => passwordEncoder.verify(input.password, user.password)
      case None => false
    }
}
