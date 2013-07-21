package controllers.api

import play.api.mvc.Action
import controllers.manager.{Jsonize, PostManager, PhotoManager}
import play.api.libs.json.Json
import models.{Comment, User, UserPost}
import play.api.libs.json.Json.JsValueWrapper
import play.api.Logger

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:10
 * To change this template use File | Settings | File Templates.
 */
object PostAPI extends MyController{

  def uploadPhoto = Authenticated(implicit req => {

    Logger.debug("Upload photo")
    val uploadedImage = PhotoManager.saveUploadedFile(userId)

    Ok(Json.obj("imageId" -> uploadedImage.id.is))
  })

  def post = Authenticated( implicit req => {
    val json = req.body.asJson.get

    val p = PostManager.postNew(userId,json)

    Ok(Json.obj("postId" -> p.id.is))

  })

  def updatePost = Authenticated(implicit req => {
    val json = req.body.asJson.get
    val postId = (json \ "postId").as[Long]
    val post = PostManager.updatePost(userId,postId,json)

    Ok(Json.obj("postId" -> post.id.is))
  })


  def getPosted(userId : Long) = Authenticated(implicit req => {
    val posts = UserPost.findUserPosts(userId)

    Ok(Json.arr(posts.map( p => {
      Json.obj(
        "postId" -> p.id.is,
        "title" -> p.title.is,
        "comment" -> p.comment.is,
        "posted" -> p.posted.is
      ).asInstanceOf[JsValueWrapper]
    }) :_* ))
  })

  def getPostDetail(postId : Long) = Authenticated(implicit req => {
    val post = UserPost.findByKey(postId).get

    val u = User.findByKey(userId).get
    val r = if(u.isAdmin){
      Jsonize.includeManageInfo(post)
    }else if(post.postUser.is == postId){
      Jsonize.allInfo(post)
    }else{
      Jsonize.restrictInfo(post)
    }

    Ok(r)

  })


  def getPostNearBy( lon : Double,lat : Double) = Authenticated(implicit req => {
    val posts = PostManager.findNearPosts(lon,lat)

    Ok( Json.arr( posts.map( p => {
      Jsonize.restrictInfo(p).asInstanceOf[JsValueWrapper]
    }) :_* ))


  })




}
