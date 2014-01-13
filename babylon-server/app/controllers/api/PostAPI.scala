package controllers.api

import play.api.mvc.Action
import controllers.manager.{Jsonize, PostManager, PhotoManager}
import play.api.libs.json.Json
import play.api.libs.json.Json.JsValueWrapper
import play.api.Logger
import jp.utokyo.babylon.db.{PrivateMessage, UserPost, User, PostCategory}

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

    Ok(Jsonize.allInfo(p));

  })

  def updatePost = Authenticated(implicit req => {
    val json = req.body.asJson.get
    val postId = (json \ "postId").as[Long]
    val post = PostManager.updatePost(userId,postId,json)

    Ok(Jsonize.allInfo(post))
  })


  def getPosted(userId : Long) = Authenticated(implicit req => {
    val posts = UserPost.findUserPosts(userId)

    Ok(Json.arr(posts.map( p => {
      Json.obj(
        "postId" -> p.id.is,
        "title" -> p.title.is,
        "comment" -> p.comment.is,
        "posted" -> p.posted.get
      ).asInstanceOf[JsValueWrapper]
    }) :_* ))
  })

  def getPostDetail(postId : Long) = Authenticated(implicit req => {
    val post = PostManager.getPost(postId)

    val u = User.findByKey(userId).get
    val r = if(u.admin.get){
      Jsonize.includeManageInfo(post)
    }else if(post.postUser.get == userId){
      Jsonize.allInfo(post)
    }else{
      Jsonize.restrictInfo(post)
    }

    Ok(r)

  })


  def getPostNearBy( lon : Double,lat : Double) = Authenticated(implicit req => {

    Logger.debug("Get near by " + lon + " : " + lat)

    val posts = PostManager.findNearPosts(lon,lat)

    Ok( Json.arr( posts.map( p => {
      val v : JsValueWrapper =  Jsonize.restrictInfo(p)
      v
    }) :_* ))


  })

  def getOwnPost(year : Int = 0,month : Int = 0) = Authenticated(implicit req => {
    Logger.debug("Get own posts")

    val posts = PostManager.getOwnPost(me.id.get, year,month)
    Ok(Json.arr(posts.map( p => {
      val v : JsValueWrapper = Jsonize.allInfo(p)
      v
    }) :_*))
  })

  def getAllOwnPost = Authenticated(implicit req => {
    Logger.debug("Get own posts")

    val posts = PostManager.getAllOwnPosts(me.id.get)
    Ok(Json.arr(posts.map( p => {
      val v : JsValueWrapper = Jsonize.allInfo(p)
      v
    }) :_*))
  })

  def commentTo(postId : Long) = Authenticated(implicit req => {

    val json = req.body.asJson.get
    val post = PostManager.getPost(postId)
    val comment = (json \ "comment").as[String]
    val c = PostManager.commentTo(me.id.get,postId,comment)
    Ok(Jsonize.allInfo(post))
  })

  def getCategoryAll = Authenticated(implicit req => {

    val categories = PostCategory.findAll()
    Ok(Json.arr(categories.map( c => {
      val v : JsValueWrapper = Jsonize.category(c)
      v
    }) :_*))

  })

  def sendMessageTo(postId : Long) = Authenticated(implicit req => {
    val json = req.body.asJson.get
    val post = PostManager.getPost(postId)
    val message = (json \ "message").as[String]
    val c = PostManager.sendMessageTo(me.id.get,postId,message)
    Ok(Jsonize.allInfo(post))
  })


}
