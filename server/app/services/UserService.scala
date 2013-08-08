package services

import com.google.inject.Inject
import repositories.UserRepository
import models.{UsernamePasswordInput, User}

class UserService @Inject()(userRepository: UserRepository) {
  def getByUsername(username: String): Option[User] =
    userRepository.findByUsername(username)

  def authenticate(input: UsernamePasswordInput): Boolean =
    userRepository.existsWithUsernameAndPassword(input.username, input.password)
}
