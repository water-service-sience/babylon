package controllers.management

import play.api.data.Form
import play.api.data.Forms._
import net.liftweb.common.Full
import jp.utokyo.babylon.db.PostCategory

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/10/15
 * Time: 0:32
 * To change this template use File | Settings | File Templates.
 */
object EditCategory extends ManagerBase {

  def updateCategoryForm = Form(
    tuple(
    "id" -> number,
    "label" -> text
    )
  )


  def list(id : Long) = AdminAuth( implicit req => {
    val categories = PostCategory.findAll()

    val form = PostCategory.findByKey(id) match{
      case Full(c) => updateCategoryForm.bind( Map("id" -> c.id.get.toString,"label" -> c.label.get))
      case _ => updateCategoryForm.bind(Map("id" -> "0","label" -> "New category"))
    }

    Ok(views.html.crud.edit_post_category(categories,form))
  })

  def update = AdminAuth( implicit req => {

    val (id,label) = updateCategoryForm.bindFromRequest.get

    PostCategory.findByKey(id) match{
      case Full(c) => {
        c.label := label
        c.save()
      }
      case _ => {
        PostCategory.createNewCategory(label)
      }
    }


    Redirect(routes.EditCategory.list(0))


  })

}
