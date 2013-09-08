package controllers.management

import play.api.mvc.{Action, Controller}
import models.{PostCategory, PostUpdate, User, UserPost}
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

  def userPostForm = Form(
    tuple(
      "category" -> number,
      "comment" -> text,
      "posted" -> jodaLocalDate,
      "inCharge" -> number
    )
  )

  def sendAdminMessageForm = Form(
      "adminComment" -> text
  )

  def postUpdateForm =  Form(
      "comment" -> text
  )

  def editPost(id : Long) = Action(implicit req => {
    val post = UserPost.findByKey(id).get

    if(req.method == "POST"){
      val form = userPostForm.bindFromRequest()

      post.category := form("category").value.get.toLong
      post.inCharge := form("inCharge").value.get.toLong

      post.save()

      Redirect(routes.EditPost.editPost(id))
    }else{
      val form = userPostForm.bind( Map(
        post.allFields.map(f => f.name -> f.get.toString) :_*
      ))

      val selections = Selections(
        PostCategory.findAll().map(pc => pc.id.is.toString -> pc.label.is),
        User.findAllManagers().map(u => u.id.is.toString -> u.nickname.is)
      )


      Ok(views.html.post_detail(post,form,postUpdateForm,sendAdminMessageForm,"",selections))

    }

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

  def sentAdminMessage( id : Long) = Action(implicit req => {
    val post = UserPost.findByKey(id).get
    val comment = sendAdminMessageForm.bindFromRequest.get

    val postUpdate = PostUpdate.create
    Redirect(routes.EditPost.editPost(id))

  })

  case class Selections(categories : Seq[(String,String)],managers : Seq[(String,String)])
}


