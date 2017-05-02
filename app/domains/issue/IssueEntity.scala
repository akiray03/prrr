package domains.issue

import spray.json._
import DefaultJsonProtocol._

case class IssueEntity(
                      id: Long,
                      number: Long,
                      userName: String,
                      repositoryName: String,
                      title: String,
                      body: String,
                      html_url: String
                      )

object IssueEntityProtocol extends DefaultJsonProtocol {
  implicit val issueEntityFormat: JsonFormat[IssueEntity] = jsonFormat7(IssueEntity.apply)
}
