package controllers.management

import net.liftweb.common.Full
import play.api.data.Form
import play.api.data.Forms._
import net.liftweb.common.Full
import jp.utokyo.babylon.db.{ContactType, UserPost, User, Contact}
import play.api.i18n.Lang
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

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
      "contactId" -> number,
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

  def updateUserInfo = Form(
    tuple(
      "username" -> text,
      "nickname" -> text,
      "admin" -> boolean,
      "manager" -> boolean
    )
  )
  def resetPasswordForm = Form(
    tuple(
      "oldPassword" -> text,
      "newPassword" -> text,
      "confirmPassword" -> text
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

  def userContact(userId : Long) = AdminAuth(implicit req => {

    User.findByKey(userId) match{
      case Full(user) => {
        val contacts = Contact.findAllFor(user)
        val addContactForm = this.addContactForm

        val contactTypes = ContactType.findAll().map(ct => ct.id.get.toString -> ct.label.get)

        Ok(views.html.user.contact(user,
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

        Contact.findByKey(contact("contactId").value.get.toLong) match{
          case Full(c) => {
            c.contact := contact("contact").value.get
            c.contactType := contact("contactType").value.get.toInt
            c.save()
          }
          case _ => {
            val c = Contact.createContact(userId,
              contact("contactType").value.get.toInt,
              contact("contact").value.get)

          }

        }




        Redirect(routes.UserPage.userContact(userId))
      }
      case _ => {
        Redirect(routes.UserPage.searchUser(""))
      }
    }
  })

  def detail(userId : Long) = AdminAuth(implicit req => {

    User.findByKey(userId) match{
      case Full(user) => {

        val form = this.updateUserInfo.bind(
          user.allFields.map(f => f.name -> f.get.toString).toMap
        )
        Ok(views.html.user.user_detail(user,form))
      }
      case _ => {
        Redirect(routes.UserPage.searchUser(""))
      }
    }


  })

  def updateUser(userId : Long ) = AdminAuth(implicit req => {
    val form = this.updateUserInfo.bindFromRequest()


    User.findByKey(userId) match{
      case Full(user) => {

        val (username,nickname,admin,manager) = form.get

        user.nickname := nickname
        user.username := username
        user.admin := admin
        user.manager := manager
        user.save()

        Redirect(routes.UserPage.detail(userId))
      }
      case _ => {
        Redirect(routes.UserPage.searchUser(""))
      }

    }

  })


  def resetPassword(userId : Long) = AdminAuth(implicit req => {
    val form = this.resetPasswordForm.bindFromRequest()


    User.findByKey(userId) match{
      case Full(user) => {
        val (oldPassword,newPassword,confirm ) = form.get

        if(newPassword != confirm){
          Redirect(routes.UserPage.searchUser(""))
        }else if(user.password.get.size == 0 || user.correctPassword_?(oldPassword)){
          User.setPassword(user,newPassword)
          Redirect(routes.UserPage.detail(userId))
        }else{
          Redirect(routes.UserPage.searchUser(""))

        }
      }
      case _ => {
        Redirect(routes.UserPage.searchUser(""))
      }

    }

  })


}
