package services.user

import domains.user.UserEntity

trait UserService {
  def listAll(): List[UserEntity]
}
