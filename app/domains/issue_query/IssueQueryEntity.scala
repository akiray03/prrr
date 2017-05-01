package domains.issue_query

import spray.json._
import DefaultJsonProtocol._
import java.time.ZonedDateTime
import library.json_protocol.ZonedDateTimeJsonProtocol._

case class IssueQueryEntity(
                             id: Option[Long] = None,
                             name: String,
                             ownerName: String,
                             repositoryName: String,
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

object IssueQueryProtocol extends DefaultJsonProtocol {
  implicit val issueQueryParameterFormat: JsonFormat[IssueQueryEntity] = jsonFormat13(IssueQueryEntity.apply)
}
