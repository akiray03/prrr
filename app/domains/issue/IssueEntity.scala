package domains.issue

import spray.json._
import DefaultJsonProtocol._

case class IssueEntity(
                      number: Long,
                      fullName: String,
                      title: String,
                      body: String,
                      html_url: String
                      )

object IssueEntityProtocol extends DefaultJsonProtocol {
  implicit val issueEntityFormat: JsonFormat[IssueEntity] = jsonFormat5(IssueEntity.apply)
}
