package controllers

import java.util.NoSuchElementException
import javax.inject._

import play.api._
import play.api.mvc._
import play.api.Logger
import play.api.libs.json
import spray.json._
import DefaultJsonProtocol._

import domains.issue_query.IssueQueryProtocol._
import domains.issue_query.IssueQueryEntity
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

  def create: Action[json.JsValue] = Action(parse.json) { request =>
    val requestBody = request.body
    Logger.info(s"request = ${requestBody.toString()}")

    try {
      val jsonAst = JsonParser(requestBody.toString())
      val issueQuery = jsonAst.convertTo[IssueQueryEntity]

      Logger.info(s"${issueQuery.toJson.compactPrint}")
      val id = issueQueryService.insert(issueQuery)
      id match {
        case Some(i) => {
          val iq = issueQueryService.fetch(i).get
          Ok(iq.toJson.prettyPrint).as(JSON)
        }
        case None => InternalServerError(s"Can't save entity: ${issueQuery.toJson.compactPrint}")
      }
    } catch {
      case e: DeserializationException => UnprocessableEntity(s"DeserializationException: ${e.getMessage}")
      case e: NoSuchElementException => UnprocessableEntity(s"NoSuchElementException: ${e.getMessage}")
    }
  }
}
