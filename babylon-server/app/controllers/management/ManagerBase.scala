package controllers.management

import play.api.mvc._
import play.api.Logger
import jp.utokyo.babylon.db.User
import org.json4s.JValue

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

  implicit def jValueToResult(v : JValue) : Result = {
    Ok(
      org.json4s.native.JsonMethods.pretty(
        org.json4s.native.JsonMethods.render(v)
      )
    )
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
