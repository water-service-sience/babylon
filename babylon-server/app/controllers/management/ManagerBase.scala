package controllers.management

import play.api.mvc._
import play.api.Logger
import models.User

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/09/04
 * Time: 1:21
 * To change this template use File | Settings | File Templates.
 */
trait ManagerBase extends Controller {

  lazy val logger = Logger.apply(getClass)

  val SessionUserId = "S_UserID"

  def userId(implicit req : Request[_]) = {
    val userId = req.session.get(SessionUserId)
    userId.getOrElse("")
  }

  def isLogin(implicit req : Request[_]) = {
    req.session.get(SessionUserId).isDefined

  }

  def me(implicit req : Request[_]) = {
    User.findByKey(userId.toLong).get
  }


  def AdminAuth(func : Request[AnyContent] => Result) : Action[AnyContent] = Action(implicit request => {
    if(!isLogin){
      logger.debug("Not login")
      Redirect(routes.LoginPage.login)
    }else{
      func(request)
    }

  })


}
