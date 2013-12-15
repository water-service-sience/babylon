package controllers.management

import net.liftweb.common.Full
import play.api.data.Form
import play.api.data.Forms._
import net.liftweb.common.Full
import jp.utokyo.babylon.db.{ContactType, UserPost, User, Contact}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/11/18
 * Time: 1:05
 * To change this template use File | Settings | File Templates.
 */
object UserPage  extends ManagerBase {


  def addContactForm = Form(
    tuple(
      "contactType" -> number,
      "contact" -> text
    )
  )

  def editContact = Form(
    tuple(
      "contactId" -> number,
      "contactType" -> number,
      "contact" -> text
    )
  )

  def searchUser(q : String) = AdminAuth(implicit req => {

    val users = if( q.length > 0) User.searchUser(q)
    else Nil
    Ok(views.html.user.search_user(q,users))
  })

  def userMenu(userId : Long) = AdminAuth(implicit req => {

    User.findByKey(userId) match{
      case Full(user) => {
        val posts = UserPost.findUserPosts(user.id.get,0,5)
        val contacts = Contact.findAllFor(user)
        val addContactForm = this.addContactForm

        val contactTypes = ContactType.findAll().map(ct => ct.id.get.toString -> ct.label.get)

        Ok(views.html.user.user_menu(user,posts,
          contacts, addContactForm,contactTypes))
      }
      case _ => {
        Redirect(routes.UserPage.searchUser(""))
      }
    }


  })

  def addContact(userId : Long) = AdminAuth(implicit req => {

    User.findByKey(userId) match{
      case Full(user) => {
        val contact = addContactForm.bindFromRequest()

        val c = Contact.createContact(userId,
          contact("contactType").value.get.toInt,
          contact("contact").value.get)



        Redirect(routes.UserPage.userMenu(userId))
      }
      case _ => {
        Redirect(routes.UserPage.searchUser(""))
      }
    }
  })

}
