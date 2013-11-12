package controllers.management

import play.api.mvc.{Action, Controller}
import models._
import controllers.manager.PostManager
import play.api.data.Form
import play.api.data.Forms._
import java.util.Date
import play.api.Logger

import play.api.libs.json.{JsValue, Json}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/07/21
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
object EditPost extends ManagerBase {



  def sendPrivateMessageForm = Form(
      "adminComment" -> text
  )

  def postUpdateForm =  Form(
    tuple(
      "comment" -> text,
      "inCharge" -> number,
      "category" -> number,
      "postStatus" -> number
    )
  )

  def editPost(id : Long) = AdminAuth(implicit req => {
    val post = UserPost.findByKey(id).get

    val form = postUpdateForm.bind( Map(
      post.allFields.map(f => f.name -> {Option(f.get).map(_.toString).getOrElse("")}) :_*
    ))

    val histories = getUpdateHistories(post)

    val selections = Selections(
      PostCategory.findAll().map(pc => pc.id.is.toString -> pc.label.get),
      User.findAllManagers().map(u => u.id.is.toString -> u.nickname.get),
      PostStatus.findAll().map(ps => ps.id.is.toString -> ps.label.get)
    )

    val privateMessages = PrivateMessage.getAllAdminComments(post.id.get)

    val message = req.session.get("message").getOrElse("")
    val selectedTab = req.session.get("selectedTab").getOrElse("info")

    Ok(views.html.post_detail(post,histories,privateMessages,
      form,postUpdateForm,sendPrivateMessageForm,message,
      selectedTab,
      selections)).
      withSession(req.session - "message" - "selectedTab")
  })

  def updatePostStatus(id : Long) = AdminAuth(implicit req => {
    val post = UserPost.findByKey(id).get

    var updateSomething = false

    val form = postUpdateForm.bindFromRequest

    val inCharge = form("inCharge").value.get.toLong
    val category = form("category").value.get.toLong
    val postStatus = form("postStatus").value.get.toLong
    val comment = form("comment").value.get

    var detail = Json.obj()

    val pu = PostUpdate.create(post,me)
    pu.actionDetail := ""

    pu.actionType := ActionType.ChangeStatus

    if(postStatus != post.postStatus.get){
      val old = post.postStatus.get
      post.postStatus := postStatus
      updateSomething = true
      detail = detail + ("postStatus" -> Json.obj("from" -> old,"to" -> postStatus))
    }

    if(category != post.category.get){
      val old = post.category.get
      post.category := category
      updateSomething = true
      detail = detail + ("category" -> Json.obj("from" -> old,"to" -> category))
    }

    if(inCharge != post.inCharge.get){
      val old = post.inCharge.get
      post.inCharge := inCharge
      updateSomething = true
      detail = detail + ("inCharge" -> Json.obj("from" -> old,"to" -> inCharge))
    }

    if(comment != null && comment.length > 0){
      pu.comment := comment
      updateSomething = true
      detail = detail ++ Json.obj("comment" -> comment)
    }

    if(updateSomething){
      pu.actionDetail := Json.stringify(detail)
      post.updated := new Date()
      pu.save()
      post.save()

      logger.debug("Update post status")
      Redirect(routes.EditPost.editPost(id)).withSession(session +
        ("message" -> "Update post status") +
        ("selectedTab" -> "info"))
    }else{
      Redirect(routes.EditPost.editPost(id)).withSession(session +
        ("message" -> "No updates") +
        ("selectedTab" -> "info"))
    }


  })


  def sendPrivateMessage( id : Long) = AdminAuth(implicit req => {
    val post = UserPost.findByKey(id).get
    val comment = sendPrivateMessageForm.bindFromRequest.get

    if(comment == null || comment.length == 0){
      Redirect(routes.EditPost.editPost(id)).withSession(
        req.session +
          ("message" -> "Please input comment") +
          ("selectedTab" -> "message"))
    }else{

      val am = PrivateMessage.create(post,me.id.get,comment)
      am.save()

      val pu = PostUpdate.create(post,me)
      pu.actionDetail := Json.stringify(Json.obj("private_message" -> comment))
      pu.actionType := ActionType.SendPrivateMessage

      pu.save

      post.unreadMessages := post.unreadMessages.get + 1
      post.updated := new Date
      post.save()

      logger.debug("Update admin message")

      Redirect(routes.EditPost.editPost(id)).withSession(
        req.session +
          ("message" -> "Send private message") +
          ("selectedTab" -> "message"))
    }
  })

  def getUpdateHistories(post : UserPost) = {
    val updates = PostUpdate.findAllOf(post)

    updates.map( u => {
      val detail = Json.parse(u.actionDetail.get)

      val builder = new StringBuilder()
      if( (detail \ "category").asOpt[JsValue].isDefined){
        val from = (detail \ "category" \ "from").as[Long]
        val to = (detail \ "category" \ "to").as[Long]
        builder.append(s"カテゴリーを'${PostCategory.label(from)}'から'${PostCategory.label(to)}'に変更")
      }

      (detail \ "inCharge").asOpt[JsValue].foreach( v => {
        val from = (v \ "from").as[Long]
        val to = (v \ "to").as[Long]

        def uname(id : Long) = User.findByKey(id).map(_.nickname.get).getOrElse("なし")
        builder.append(s"担当を'${uname(from)}'から'${uname(to)}'へ変更")
      })

      (detail \ "postStatus").asOpt[JsValue].foreach( v => {
        val from = (v \ "from").as[Long]
        val to = (v \ "to").as[Long]

        def plabel(id : Long) = PostStatus.findByKey(id).map(_.label.get).getOrElse("None")
        builder.append(s"担当を'${plabel(from)}'から'${plabel(to)}'へ変更")
      })

      (detail \ "private_message").asOpt[String].foreach( v => {
        builder.append(s"プライベートメッセージ送信")
      })



      UpdateHistory(u.editor.obj.get,u.comment.get,builder.toString() ,u.edited.get)
    })
  }

  case class UpdateHistory(editor : User,comment : String,action : String, updated : Date)


  case class Selections(categories : Seq[(String,String)],managers : Seq[(String,String)],postStatuses : Seq[(String,String)])
}


