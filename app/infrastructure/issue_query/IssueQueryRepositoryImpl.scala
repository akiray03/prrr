package infrastructure.issue_query

import java.sql.SQLException
import java.time.ZonedDateTime

import spray.json._
import DefaultJsonProtocol._
import library.json_protocol.ZonedDateTimeJsonProtocol._

import play.api.Logger
import javax.inject._

import scalikejdbc._
import skinny.orm._
import library.skinny.features.TimestampsFeature
import domains.issue_query.{IssueQueryEntity, IssueQueryRepository}


case class IssueQueryPersistenceEntity(
                                        id: Option[Long] = None,
                                        name: String,
                                        ownerName: String,
                                        repositoryName: String,
                                        parameters: String
                                      )
case class Parameter(
                       milestone: Option[String] = None,
                       state: Option[String] = None,
                       assignee: Option[List[String]] = None,
                       creator: Option[List[String]] = None,
                       mentioned: Option[List[String]] = None,
                       labels: Option[List[String]] = None,
                       sort: Option[String] = None,
                       direction: Option[String] = None,
                       since: Option[ZonedDateTime] = None
                    )

object IssueQueryParameterProtocol extends DefaultJsonProtocol {
  implicit val format: JsonFormat[Parameter] = jsonFormat9(Parameter.apply)
}
import IssueQueryParameterProtocol._

class IssueQueryPersistenceRepository
  extends SkinnyMapper[IssueQueryPersistenceEntity]
    with TimestampsFeature[IssueQueryPersistenceEntity] {

  override lazy val tableName = "issue_queries"
  override lazy val defaultAlias = createAlias("iq")

  override def extract(rs: WrappedResultSet, rn: ResultName[IssueQueryPersistenceEntity]): IssueQueryPersistenceEntity = {
    val issueQuery = IssueQueryPersistenceEntity(
      id = Some(rs.long(rn.id)),
      name = rs.string(rn.name),
      ownerName = rs.string(rn.ownerName),
      repositoryName = rs.string(rn.repositoryName),
      parameters = rs.string(rn.parameters)
    )

    issueQuery
  }

  def listAll(): List[IssueQueryPersistenceEntity] = findAll()

  def insert(entity: IssueQueryPersistenceEntity): Option[Long] = {
    try {
      Some(
        createWithNamedValues(
          column.name -> entity.name,
          column.ownerName -> entity.ownerName,
          column.repositoryName -> entity.repositoryName,
          column.parameters -> entity.parameters
        )
      )
    } catch {
      // Duplicate Entry の場合は None を返す
      case e: SQLException if e.getErrorCode == 1062 => {
        Logger.warn(e.getLocalizedMessage)
        None
      }
    }
  }
}

class IssueQueryRepositoryImpl @Inject()(issueQueryPersistenceRepository: IssueQueryPersistenceRepository)
  extends IssueQueryRepository {

  def listAll(): List[IssueQueryEntity] = {
    issueQueryPersistenceRepository.listAll().map { e =>
      val parameter = JsonParser(e.parameters).convertTo[Parameter]

      IssueQueryEntity(
        id = e.id,
        name = e.name,
        ownerName = e.ownerName,
        repositoryName = e.repositoryName,
        milestone = parameter.milestone,
        state = parameter.state,
        assignee = parameter.assignee,
        creator = parameter.creator,
        mentioned = parameter.mentioned,
        labels = parameter.labels,
        sort = parameter.sort,
        direction = parameter.direction,
        since = parameter.since
      )
    }
  }

  def insert(issueQuery: IssueQueryEntity): Option[Long] = {
    val parameter = Parameter(
      milestone = issueQuery.milestone,
      state = issueQuery.state,
      assignee = issueQuery.assignee,
      creator = issueQuery.creator,
      mentioned = issueQuery.mentioned,
      labels = issueQuery.labels,
      sort = issueQuery.sort,
      direction = issueQuery.direction,
      since = issueQuery.since
    )
    val parameterJson = parameter.toJson.prettyPrint

    val entity = IssueQueryPersistenceEntity(
      name = issueQuery.name,
      ownerName = issueQuery.ownerName,
      repositoryName = issueQuery.repositoryName,
      parameters = parameterJson
    )

    issueQueryPersistenceRepository.insert(entity)
  }


  def fetch(id: Long): Option[IssueQueryEntity] = {
    listAll().find(p => p.id.getOrElse(0) == id)
  }
}
