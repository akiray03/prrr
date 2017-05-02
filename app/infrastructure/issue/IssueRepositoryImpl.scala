package infrastructure.issue

import javax.inject.Inject

import play.api.Logger
import spray.json._
import DefaultJsonProtocol._
import library.json_protocol.ZonedDateTimeJsonProtocol._
import skinny.http.{HTTP, Request, Response}
import scala.collection.mutable

import response.{IssueListResponse, IssueResponse}
import response.IssueResponseProtocol._

import domains.issue.{IssueEntity, IssueRepository}
import domains.issue_query.IssueQueryEntity

class IssueRepositoryImpl @Inject()(
                                     configuration: play.api.Configuration
                                   ) extends IssueRepository{
  private def authorizationHeader(): String = {
    val githubToken = configuration.getString("secrets.github_token")
    if (githubToken.isEmpty) {
      throw new Exception("GITHUB TOKEN が設定されてないぜ")
    }
    f"token ${githubToken.get}%s"
  }

  override def fetchIssues(issueQuery: IssueQueryEntity): List[IssueEntity] = {
    val req = queryBuilder(issueQuery).header("Authorization", authorizationHeader())
    val response: Response = HTTP.get(req)
    val body = """{"issues":""" + response.textBody + """}"""

    Logger.debug(body)

    val jsonAst = JsonParser(body)
    val issues = jsonAst.convertTo[IssueListResponse]

    convertToIssueEntities(issues)
  }

  private def queryBuilder(issueQuery: IssueQueryEntity): Request = {
    val url = f"https://api.github.com/repos/${issueQuery.ownerName}/${issueQuery.repositoryName}/issues"
    val req = Request(url)

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

  private def convertToIssueEntities(issues: IssueListResponse): List[IssueEntity] = {
    val repoNames = fetchRepositoryNames(issues)

    issues.issues.map(i =>
      IssueEntity(
        number = i.number,
        fullName = repoNames.getOrElse(i.repository_url, ""),
        title = i.title,
        body = i.body,
        html_url = i.html_url,
        created_at = i.created_at,
        updated_at = i.updated_at
      )
    )
  }

  private def fetchRepositoryNames(issues: IssueListResponse): Map[String, String] = {
    val repoNames = mutable.HashMap.empty[String, String]

    issues.issues.foreach(issue => {
      if (!repoNames.contains(issue.repository_url)) {
        val repoName = fetchRepositoryName(issue.repository_url)
        repoNames.put(issue.repository_url, repoName)
      }
    })

    repoNames.toMap[String, String]
  }

  private def fetchRepositoryName(repoURL: String): String = {
    val req = Request(repoURL).header("Authorization", authorizationHeader())
    val response: Response = HTTP.get(req)

    val jsonAst = JsonParser(response.textBody)
    jsonAst.asJsObject.getFields("full_name") match {
      case Seq(JsString(full_name)) => full_name
      case _ => ""
    }
  }
}
