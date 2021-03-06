package controllers.management

import play.api.mvc.{Action, Controller}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import jp.utokyo.babylon.db.User
import play.api.i18n.Lang
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/09/07
 * Time: 18:36
 * To change this template use File | Settings | File Templates.
 */
object LoginPage extends ManagerBase {
  def loginForm = Form(
    tuple(
      "username" -> text.verifying(nonEmpty),
      "password" -> text.verifying(nonEmpty)
    )
  )

  def p_login = login

  def login = Action(implicit req => {

    if(req.method == "POST"){

      val form = loginForm.bindFromRequest()


      val username = form("username").value.get
      val password = form("password").value.get


      val user = User.findByUsername(username)
      if(!user.isDefined){
        Ok(views.html.login("User not found or wrong password.",form))
      }else if(!user.get.correctPassword_?(password)){
        Ok(views.html.login("User not found or wrong password..",form))
      }else{
        Redirect(routes.TopPage.topPage).withSession(SessionUserId -> user.get.id.get.toString)
      }
    }else{

      val form = loginForm

      Ok(views.html.login("",form))
    }


  })

  def logout = Action(req => {
    Redirect(routes.LoginPage.login).withNewSession
  })

}
