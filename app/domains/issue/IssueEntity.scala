package domains.issue

import java.time.ZonedDateTime

import spray.json._
import DefaultJsonProtocol._
import library.json_protocol.ZonedDateTimeJsonProtocol._

case class IssueEntity(
                        number: Long,
                        fullName: String,
                        title: String,
                        body: String,
                        html_url: String,
                        created_at: ZonedDateTime,
                        updated_at: ZonedDateTime
                      )

object IssueEntityProtocol extends DefaultJsonProtocol {
  implicit val issueEntityFormat: JsonFormat[IssueEntity] = jsonFormat7(IssueEntity.apply)
}
