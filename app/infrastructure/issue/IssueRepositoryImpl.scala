package infrastructure.issue

import javax.inject.Inject

import domains.issue.IssueRepository
import domains.issue_query.IssueQueryEntity
import skinny.http.{HTTP, Request, Response}

class IssueRepositoryImpl @Inject()(
                                     configuration: play.api.Configuration
                                   ) extends IssueRepository{
  override def fetchIssues(issueQuery: IssueQueryEntity): String = {
    val githubToken = configuration.getString("secrets.github_token")
    if (githubToken.isEmpty) {
      throw new Exception("GITHUB TOKEN が設定されてないぜ")
    }
    val authHeader = f"token ${githubToken.get}%s"

    var req = queryBuilder(issueQuery).header("Authorization", authHeader)
    val response: Response = HTTP.get(req)

    response.textBody
  }

  private def queryBuilder(issueQuery: IssueQueryEntity): Request = {
    val url = f"https://api.github.com/repos/${issueQuery.ownerName}/${issueQuery.repositoryName}/issues"
    var req = Request(url)

    val state = issueQuery.state.getOrElse("open")
    req.queryParam("state", state)

    val assignees = issueQuery.assignee.getOrElse(List())
    for (assignee <- assignees) {
      req.queryParam("assignee", assignee)
    }

    val creators = issueQuery.creator.getOrElse(List())
    for (creator <- creators) {
      req.queryParam("creator", creator)
    }

    val mentioned = issueQuery.mentioned.getOrElse(List())
    for (m <- mentioned) {
      req.queryParam("mentioned", m)
    }

    val labels = issueQuery.labels.getOrElse(List())
    if (labels.nonEmpty) {
      req.queryParam("labels", labels.mkString(","))
    }

    val sort = issueQuery.sort.getOrElse("created")
    req.queryParam("sort", sort)

    val direction = issueQuery.direction.getOrElse("desc")
    req.queryParam("direction", direction)

    issueQuery.since match {
      case Some(since) => req.queryParam("since", since.toString)
      case None =>
    }

    req
  }
}
