package domains.user

import spray.json._

case class UserEntity(
                     id: Option[Long] = None,
                     name: String,
                     role: String
                     )

object UserProtocol extends DefaultJsonProtocol {
  implicit val userFormat: JsonFormat[UserEntity] = jsonFormat3(UserEntity.apply)
}
