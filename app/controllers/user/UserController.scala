package controllers.user

import javax.inject._

import play.api._
import play.api.mvc._
import services.user.UserService

@Singleton
class UserController @Inject()(userService: UserService) extends Controller{

  def index = Action {
    val users = userService.listAll()
    Ok("users")
  }
}
