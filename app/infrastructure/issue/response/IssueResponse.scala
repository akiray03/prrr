package infrastructure.issue.response

import spray.json._
import DefaultJsonProtocol._
import java.time.ZonedDateTime
import library.json_protocol.ZonedDateTimeJsonProtocol._

/**
  * GitHub API v3 の返すユーザ情報
  *
  * @param login            GitHubのユーザ名
  * @param id               GitHub内部のユーザID
  * @param avatar_url       アバターURL
  * @param gravatar_id      GravatarのID
  * @param url              ユーザ情報取得APIのURL
  * @param html_url         ユーザページのURL
  * @param followers_url
  * @param following_url
  * @param gists_url
  * @param starred_url
  * @param subscriptions_url
  * @param organizations_url
  * @param repos_url
  * @param events_url
  * @param received_events_url
  * @param `type`
  * @param site_admin
  */
case class GitHubUser(
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

/**
  * GitHub API v3 の返すラベル情報
  *
  * @param id       GitHub内部のラベルID
  * @param url      ラベル情報APIのURL
  * @param name     ラベル名
  * @param color    ラベル色
  * @param default  デフォルトか否か
  */
case class IssueLabel(
                       id: Long,
                       url: String,
                       name: String,
                       color: String,
                       default: Boolean
                     )

/**
  * GitHub API v3 の返すマイルストーン情報
  *
  * @param url            マイルストーンの情報取得APIのURL
  * @param html_url       マイルストーンのURL
  * @param id             マイルストーンのID
  * @param number         マイルストーンの番号
  * @param state          マイルストーンの状態
  * @param title          マイルストーン名称
  * @param open_issues    オープンされているIssueの数
  * @param closed_issues  クローズされたIssueの数
  * @param created_at     マイルストーンの作成日時
  * @param updated_at     マイルストーンの更新日時
  * @param closed_at      マイルストーンのクローズ日時
  * @param due_on         マイルストーンの〆切日時
  */
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

/**
  * GitHub API v3 の返すプルリクエスト情報
  *
  * @param url        Pull Request情報APIのURL
  * @param html_url   Pull Request の URL
  * @param diff_url   diff URL
  * @param patch_url  patch URL
  */
case class IssuePullRequest(
                             url: String,
                             html_url: String,
                             diff_url: String,
                             patch_url: String
                           )

/**
  * GitHub API v3 の返す Issue 詳細情報
  *
  * @param id
  * @param url
  * @param repository_url
  * @param labels_url
  * @param comments_url
  * @param events_url
  * @param html_url
  * @param number
  * @param state
  * @param title
  * @param body
  * @param user
  * @param labels
  * @param assignee
  * @param milestone
  * @param locked
  * @param comments
  * @param pull_request
  * @param closed_at
  * @param created_at
  * @param updated_at
  * @param assignees
  */
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
                          user: GitHubUser,
                          labels: List[IssueLabel],
                          assignee: Option[GitHubUser],
                          milestone: Option[IssueMilestone],
                          locked: Boolean,
                          comments: Long,
                          pull_request: IssuePullRequest,
                          closed_at: Option[ZonedDateTime] = None,
                          created_at: ZonedDateTime,
                          updated_at: ZonedDateTime,
                          assignees: List[GitHubUser]
                        )

/**
  * GitHub API v3 の返す Issue 一覧
  *
  * @param issues
  */
case class IssueListResponse(issues: List[IssueResponse])


object IssueResponseProtocol extends DefaultJsonProtocol {
  implicit val issueUserFormatter: JsonFormat[GitHubUser] = jsonFormat17(GitHubUser.apply)
  implicit val issueLabelFormatter: JsonFormat[IssueLabel] = jsonFormat5(IssueLabel.apply)
  implicit val issueMilestoneFormatter: JsonFormat[IssueMilestone] = jsonFormat12(IssueMilestone.apply)
  implicit val issuePullRequestFormatter: JsonFormat[IssuePullRequest] = jsonFormat4(IssuePullRequest.apply)
  implicit val issueResponseFormatter: JsonFormat[IssueResponse] = jsonFormat22(IssueResponse.apply)
  implicit val issueListResponseFormatter: JsonFormat[IssueListResponse] = jsonFormat1(IssueListResponse.apply)
}
