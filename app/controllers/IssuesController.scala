package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import services.issue.IssueService
import skinny.http._

@Singleton
class IssuesController @Inject()(issueService: IssueService) extends Controller {

  def index = Action {
    Ok(issueService.listAll())
  }
}
