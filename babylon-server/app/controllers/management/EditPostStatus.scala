package controllers.management

import jp.utokyo.babylon.db.PostStatus
import jp.utokyo.babylon.util.Jsonizer
import play.api.data.Form
import play.api.data.Forms._
import net.liftweb.common.Full

/**
 * Created by takezoux2 on 13/12/09.
 */
object EditPostStatus extends ManagerBase {


  def updateStatusForm = Form(
    tuple(
      "id" -> number,
      "label" -> text
    )
  )


  def list(id : Long) = AdminAuth( implicit req => {
    val posts = PostStatus.findAll()

    val form = PostStatus.findByKey(id) match{
      case Full(c) => updateStatusForm.bind( Map("id" -> c.id.get.toString,"label" -> c.label.get))
      case _ => updateStatusForm.bind(Map("id" -> "0","label" -> "New status"))
    }

    Ok(views.html.crud.edit_post_status(posts,form))
  })

  def update = AdminAuth( implicit req => {
    val (id,label) = updateStatusForm.bindFromRequest.get
    PostStatus.findByKey(id) match{
      case Full(c) => {
        c.label := label
        c.save()
      }
      case _ => {
        PostStatus.createNewStatus(label)
      }
    }


    Redirect(routes.EditPostStatus.list(0))


  })

}
