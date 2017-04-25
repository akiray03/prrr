package services.user

import javax.inject.Inject

import domains.user.{UserEntity, UserRepository}

class UserServiceImpl @Inject()(userRepository: UserRepository) extends UserService {
  override def listAll(): List[UserEntity] = {
    userRepository.listAll()
  }
}
