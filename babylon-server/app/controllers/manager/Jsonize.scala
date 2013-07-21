package controllers.manager

import models.{User, UserPost, Comment}
import play.api.libs.json.Json

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
      "postId" -> p.id.is,
      "title" -> p.title.is,
      "comment" -> p.comment.is,
      "posted" -> p.posted.is,
      "comments" -> comments(p),
      "hasGps" -> p.hasGpsInfo.is,
      "longitude" -> p.longitude.is,
      "latitude" -> p.latitude.is
    )
  }

  def restrictInfo(p : UserPost) = {
    Json.obj(
      "postId" -> p.id.is,
      "title" -> p.title.is,
      "comment" -> p.comment.is,
      "posted" -> p.posted.is,
      "comments" -> comments(p),
      "imageFile" -> p.image.obj.get.fileKey.is,
      "hasGps" -> p.hasGpsInfo.is,
      "longitude" -> p.longitude.is,
      "latitude" -> p.latitude.is
    )
  }
  def includeManageInfo(p : UserPost) = {

    Json.obj(
      "postId" -> p.id.is,
      "title" -> p.title.is,
      "comment" -> p.comment.is,
      "posted" -> p.posted.is,
      "comments" -> comments(p),
      "hasGps" -> p.hasGpsInfo.is,
      "longitude" -> p.longitude.is,
      "latitude" -> p.latitude.is
    )
  }
  def comments(post : UserPost) = {
    val comments = Comment.getAllComments(post.id.is)
    Json.arr(comments.map(comment _))
  }

  def comment( c : Comment) = {
    Json.obj(
      "commentId" -> c.id.is,
      "user" -> user(c.commentUser.obj.get),
      "comment" -> c.comment.is,
      "commented" -> c.commented.is
    )
  }

  def user(u : User) = {
    Json.obj("userId" -> u.id.is,
    "nickname" -> u.nickname.is)
  }


}
