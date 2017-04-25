package domains.user

case class UserEntity(
                     id: Option[Long] = None,
                     name: String,
                     role: String
                     )
