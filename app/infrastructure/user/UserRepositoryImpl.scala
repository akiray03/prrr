package infrastructure.user

import java.sql.SQLException
import play.api.Logger

import scalikejdbc._
import skinny.orm._
import skinny.orm.feature.TimestampsFeature

import domains.user._

class UserRepositoryImpl extends SkinnyMapper[UserEntity] with TimestampsFeature[UserEntity] with UserRepository {
  override lazy val tableName = "users"
  override lazy val defaultAlias = createAlias("u")
  private[this] lazy val a = defaultAlias

  override def extract(rs: WrappedResultSet, rn: ResultName[UserEntity]): UserEntity = UserEntity(
    id = Some(rs.int(rn.id)),
    name = rs.string(rn.name),
    role = rs.string(rn.role)
  )

  def listAll(): List[UserEntity] = findAll()

  def insert(user: UserEntity): Option[Long] = {
    try {
      Some(
        createWithNamedValues(
          column.name -> user.name,
          column.role -> user.role
        )
      )
    } catch {
      // Duplicate Entryだけは拾ってあげる
      case e: SQLException if e.getErrorCode == 1062 => {
        Logger.warn(e.getLocalizedMessage)
        None
      }
    }
  }
}
