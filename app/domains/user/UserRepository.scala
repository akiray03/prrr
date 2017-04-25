package domains.user

trait UserRepository {
  def listAll(): List[UserEntity]
  def insert(user: UserEntity): Option[Long]
}
