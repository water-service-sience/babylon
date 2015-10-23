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
object RegisterPage extends ManagerBase {
  def createUser = Form(
    tuple(
      "username" -> text.verifying(minLength(4)),
      "password1" -> text.verifying(minLength(4)),
      "password2" -> text.verifying(minLength(4)),
      "isAdmin" -> boolean
    )
  )

  def p_register = register

  def register = Action(implicit req => {

    if(req.method == "POST"){

      val form = createUser.bindFromRequest()

      if(form.hasErrors){
        Ok(views.html.register("",form))
      }else{
        val username = form("username").value.get
        val password1 = form("password1").value.get
        val password2 = form("password2").value.get
        val isAdmin = form("isAdmin").value.map(_.toBoolean).getOrElse(false)

        if(password1 != password2){
          Ok(views.html.register("Password is not match!",form))

        }else{

          val u = User.create(username,username)

          u.manager := true
          u.username := username
          u.password := password1
          u.admin := isAdmin
          u.save()

          Redirect(routes.TopPage.topPage).withSession(SessionUserId -> u.id.get.toString)
        }
      }
    }else{

      val form = createUser

      Ok(views.html.register("",form))
    }





  })

}
