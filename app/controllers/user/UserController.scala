package controllers.user

import javax.inject._
import play.api._
import play.api.mvc._
import spray.json._
import DefaultJsonProtocol._

import domains.user.UserEntity
import domains.user.UserProtocol._
import services.user.UserService

@Singleton
class UserController @Inject()(userService: UserService) extends Controller{

  def index = Action {
    val users = userService.listAll()
    Ok(JsArray(users.map{ user => user.toJson }.toVector).prettyPrint)
  }
}
