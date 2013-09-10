package controllers.manager

import models._
import play.api.libs.json.{JsValue, Json}
import play.api.libs.json.Json.JsValueWrapper

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 1:39
 * To change this template use File | Settings | File Templates.
 */
object Jsonize {

  def allInfo(p : UserPost) = {
    Json.obj(
      "id" -> p.id.is,
      "postId" -> p.id.is,
      "userId" -> p.postUser.get,
      "user" -> user(p.postUser.obj.get),
      "title" -> p.title.is,
      "comment" -> p.comment.is,
      "posted" -> p.posted.is,
      "comments" -> comments(p),
      "goodness" -> p.goodness.get,
      "imageFile" -> p.image.obj.get.fileKey.is,
      "hasGps" -> p.hasGpsInfo.is,
      "longitude" -> p.longitude.is,
      "latitude" -> p.latitude.is,
      "unreadComments" -> p.unreadComments.is,
      "unreadMessages" -> p.unreadMessages.is,
      "privateMessages" -> privateMessages(p),
      "category" -> {
        val j : JsValue = p.category.map(category(_)).openOr( Json.obj("id" -> 0,"lable" -> "None"))
        j
      }
    )
  }

  def restrictInfo(p : UserPost)  = {
    allInfo(p)
//    Json.obj(
//      "postId" -> p.id.is,
//      "userId" -> p.postUser.get,
//      "title" -> p.title.is,
//      "comment" -> p.comment.is,
//      "posted" -> p.posted.is,
//      "comments" -> comments(p),
//      "imageFile" -> p.image.obj.get.fileKey.is,
//      "hasGps" -> p.hasGpsInfo.is,
//      "longitude" -> p.longitude.is,
//      "latitude" -> p.latitude.is
//    )
  }
  def includeManageInfo(p : UserPost)  = {
    allInfo(p)

//    Json.obj(
//      "postId" -> p.id.is,
//      "title" -> p.title.is,
//      "userId" -> p.postUser.get,
//      "comment" -> p.comment.is,
//      "posted" -> p.posted.is,
//      "comments" -> comments(p),
//      "imageFile" -> p.image.obj.get.fileKey.is,
//      "hasGps" -> p.hasGpsInfo.is,
//      "longitude" -> p.longitude.is,
//      "latitude" -> p.latitude.is
//    )
  }
  def comments(post : UserPost) = {
    val comments = Comment.getAllComments(post.id.is)
    comments.map(comment _)
  }
  def comment( c : Comment) = {
    Json.obj(
      "commentId" -> c.id.is,
      "user" -> user(c.commentUser.obj.get),
      "comment" -> c.comment.is,
      "commented" -> c.commented.is
    )
  }

  def updates(post : UserPost) = {
    val postUpdates = PostUpdate.findAllOf(post)
    postUpdates.map(postUpdate _)
  }

  def privateMessages( p : UserPost) = {
    val pms = PrivateMessage.getAllAdminComments(p.id.get)
    pms.map(privateMessage(_))
  }

  def privateMessage( am : PrivateMessage) = {

    Json.obj(
      "privateMessageId" -> am.id.get,
      "sender" -> user(am.commentUser.obj.get),
      "message" -> am.comment.get,
      "sent" -> am.commented.get
    )

  }

  def postUpdate( pu : PostUpdate) = {
    Json.obj(
      "postId" -> pu.userPost.get,
      "editor" -> user(pu.editor.obj.get),
      "comment" -> pu.comment.get,
      "replyTo" -> pu.replyTo.get,
      "updated" -> pu.edited.get
    )
  }


  def user(u : User) = {
    Json.obj("userId" -> u.id.is,
    "nickname" -> u.nickname.is)
  }

  def category(cat : PostCategory) = {
    Json.obj("id" -> cat.id.get,"label" -> cat.label.is)
  }


}
