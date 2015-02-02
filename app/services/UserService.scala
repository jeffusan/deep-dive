package services

import models.User
import models.UserRepository
import play.api.Logger

class UserService(userRepository: UserRepository) {

  def authenticate(email: String, password: String): Option[User] = {
    Logger.debug("Authenticating " + email + " now")
    userRepository.findOneByEmailAndPassword(email, password)
  }

  def findOneById(id: Long): Option[User] = {
    userRepository.findOneById(id)
  }
}
