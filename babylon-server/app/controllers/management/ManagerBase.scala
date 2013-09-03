package controllers.management

import play.api.mvc._

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/09/04
 * Time: 1:21
 * To change this template use File | Settings | File Templates.
 */
trait ManagerBase extends Controller {

  val CookieUserId = "C_UserID"

  def userId(implicit req : Request[_]) = {
    val userId = req.cookies(CookieUserId)
    userId.value.toString
  }

  def isLogin(implicit req : Request[_]) = {
    req.cookies.get(CookieUserId).isDefined

  }

  def AdminAuth(func : Request[AnyContent] => Result) : Action[AnyContent] = Action(implicit request => {
    if(!isLogin){
      Redirect(routes.ManagementLogin.login)
    }else{
      func(request)
    }

  })

}
