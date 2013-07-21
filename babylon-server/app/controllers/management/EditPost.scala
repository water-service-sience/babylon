package controllers.management

import play.api.mvc.{Action, Controller}
import models.{PostUpdate, User, UserPost}
import controllers.manager.PostManager
import play.api.data.Form
import play.api.data.Forms._
import java.util.Date

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/07/21
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
object EditPost extends Controller {

  // TODO fix
  def editor = User.findByKey(1).get

  def postUpdateForm =  Form(
      "comment" -> text
  )

  def editPost(id : Long) = Action({
    val post = UserPost.findByKey(id).get

    val form = Form(
      tuple(
        "category" -> number,
        "comment" -> text,
        "posted" -> jodaLocalDate
      )
    ).bind( Map(
      post.allFields.map(f => f.name -> f.get.toString) :_*
    ))



    Ok(views.html.post_detail(post,form,postUpdateForm,""))

  })

  def updatePostStatus(id : Long) = Action(implicit req => {
    val post = UserPost.findByKey(id).get

    val comment = postUpdateForm.bindFromRequest.get

    val pu = PostUpdate.create
    pu.editor := editor.id.get
    pu.userPost := id
    pu.comment := comment
    pu.edited := new Date
    pu.save()

    Redirect(routes.EditPost.editPost(id))
  })

  def sendMessage(id : Long) = Action({
    val post = UserPost.findByKey(id).get

    Redirect(routes.EditPost.editPost(id))
  })
}
