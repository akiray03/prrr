package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import services.issue.IssueService
import services.issue_query.IssueQueryService
import skinny.http._

import spray.json._
import DefaultJsonProtocol._

import domains.issue.IssueEntityProtocol._
import domains.issue_query.IssueQueryProtocol._
import services.issue_query.IssueQueryService

@Singleton
class IssuesController @Inject()(issueQueryService: IssueQueryService, issueService: IssueService) extends Controller {

  def index(issue_query_id: Long) = Action {
    val query = issueQueryService.fetch(issue_query_id)
    query match {
      case Some(x) => {
        val issues = issueService.fetchIssues(x)
        // Ok(issues)
        Ok(JsArray(issues.map{ issue => issue.toJson }.toVector).prettyPrint)
      }
      case None => NotFound("""{"status": "error", "code": "Not Found"}""")
    }
  }
}
