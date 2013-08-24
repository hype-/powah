package powah.user

import com.google.inject.Inject
import powah.common.PasswordEncoder

class AuthenticationService @Inject()(
  userRepository: UserRepository,
  passwordEncoder: PasswordEncoder
) {
  def authenticate(input: UsernamePasswordInput): Boolean =
    userRepository.findByUsername(input.username) match {
      case Some(user) => passwordEncoder.verify(input.password, user.password)
      case None => false
    }
}
