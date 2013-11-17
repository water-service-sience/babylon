package controllers.management

import models.{UserPost, User}
import net.liftweb.common.Full

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/11/18
 * Time: 1:05
 * To change this template use File | Settings | File Templates.
 */
object UserPage  extends ManagerBase {


  def searchUser(q : String) = AdminAuth(implicit req => {


    val users = User.searchUser(q : String)


    Ok(views.html.user.search_user(q,users))
  })

  def userMenu(userId : Long) = AdminAuth(implicit req => {

    User.findByKey(userId) match{
      case Full(user) => {
        val posts = UserPost.findUserPosts(user.id.get,0,5)


        Ok(views.html.user.user_menu(user,posts))
      }
      case _ => {
        Redirect(routes.UserPage.searchUser(""))
      }
    }


  })

}
