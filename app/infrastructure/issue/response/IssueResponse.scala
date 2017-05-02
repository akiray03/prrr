package infrastructure.issue.response

import spray.json._
import DefaultJsonProtocol._
import java.time.ZonedDateTime
import library.json_protocol.ZonedDateTimeJsonProtocol._

case class IssueUser(
                      login: String,
                      id: Long,
                      avatar_url: String,
                      gravatar_id: String,
                      url: String,
                      html_url: String,
                      followers_url: String,
                      following_url: String,
                      gists_url: String,
                      starred_url: String,
                      subscriptions_url: String,
                      organizations_url: String,
                      repos_url: String,
                      events_url: String,
                      received_events_url: String,
                      `type`: String,
                      site_admin: Boolean
                    )
case class IssueLabel(
                       id: Long,
                       url: String,
                       name: String,
                       color: String,
                       default: Boolean
                     )
case class IssueMilestone(
                           url: String,
                           html_url: String,
                           id: Long,
                           number: Long,
                           state: String,
                           title: String,
                           open_issues: Long,
                           closed_issues: Long,
                           created_at: ZonedDateTime,
                           updated_at: ZonedDateTime,
                           closed_at: ZonedDateTime,
                           due_on: ZonedDateTime
                         )
case class IssuePullRequest(
                             url: String,
                             html_url: String,
                             diff_url: String,
                             patch_url: String
                           )

case class IssueResponse(
                          id: Long,
                          url: String,
                          repository_url: String,
                          labels_url: String,
                          comments_url: String,
                          events_url: String,
                          html_url: String,
                          number: Long,
                          state: String,
                          title: String,
                          body: String,
                          user: IssueUser,
                          labels: List[IssueLabel],
                          assignee: Option[IssueUser],
                          milestone: Option[IssueMilestone],
                          locked: Boolean,
                          comments: Long,
                          pull_request: IssuePullRequest,
                          closed_at: Option[ZonedDateTime] = None,
                          created_at: ZonedDateTime,
                          updated_at: ZonedDateTime,
                          assignees: List[IssueUser]
                        )

case class IssueListResponse(issues: List[IssueResponse])

object IssueResponseProtocol extends DefaultJsonProtocol {
  implicit val issueUserFormatter: JsonFormat[IssueUser] = jsonFormat17(IssueUser.apply)
  implicit val issueLabelFormatter: JsonFormat[IssueLabel] = jsonFormat5(IssueLabel.apply)
  implicit val issueMilestoneFormatter: JsonFormat[IssueMilestone] = jsonFormat12(IssueMilestone.apply)
  implicit val issuePullRequestFormatter: JsonFormat[IssuePullRequest] = jsonFormat4(IssuePullRequest.apply)
  implicit val issueResponseFormatter: JsonFormat[IssueResponse] = jsonFormat22(IssueResponse.apply)
  implicit val issueListResponseFormatter: JsonFormat[IssueListResponse] = jsonFormat1(IssueListResponse.apply)
}
