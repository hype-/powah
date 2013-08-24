package powah.user

import com.google.inject.Inject

class UserService @Inject()(userRepository: UserRepository) {
  def getByUsername(username: String): Option[User] =
    userRepository.findByUsername(username)
}
