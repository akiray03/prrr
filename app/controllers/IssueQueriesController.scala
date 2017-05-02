package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import spray.json._
import DefaultJsonProtocol._

import domains.issue_query.IssueQueryProtocol._
import services.issue_query.IssueQueryService

@Singleton
class IssueQueriesController @Inject()(issueQueryService: IssueQueryService) extends Controller {

  def index = Action {
    val queries = issueQueryService.listAll()
    Ok(JsArray(queries.map{ query => query.toJson }.toVector).prettyPrint)
  }

  def show(id: Long) = Action {
    val query = issueQueryService.fetch(id)
    query match {
      case Some(x) => Ok(x.toJson.prettyPrint)
      case None => NotFound("""{"status": "error", "code": "Not Found"}""")
    }
  }

}
