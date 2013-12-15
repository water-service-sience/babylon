package controllers.management

import jp.utokyo.babylon.db.{ContactType, PostStatus}
import jp.utokyo.babylon.util.Jsonizer
import play.api.data.Form
import play.api.data.Forms._
import net.liftweb.common.Full

/**
 * Created by takezoux2 on 13/12/09.
 */
object EditContactType extends ManagerBase {


  def updateContactTypeForm = Form(
    tuple(
      "id" -> number,
      "label" -> text
    )
  )


  def list(id : Long) = AdminAuth( implicit req => {
    val posts = ContactType.findAll()

    val form = ContactType.findByKey(id) match{
      case Full(c) => updateContactTypeForm.bind( Map("id" -> c.id.get.toString,"label" -> c.label.get))
      case _ => updateContactTypeForm.bind(Map("id" -> "0","label" -> "New status"))
    }

    Ok(views.html.crud.edit_contact_type(posts,form))
  })

  def update = AdminAuth( implicit req => {
    val (id,label) = updateContactTypeForm.bindFromRequest.get
    ContactType.findByKey(id) match{
      case Full(c) => {
        c.label := label
        c.save()
      }
      case _ => {
        ContactType.createNewContactType(label)
      }
    }


    Redirect(routes.EditContactType.list(0))


  })

}
