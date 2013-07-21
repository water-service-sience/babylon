package controllers.manager

import models.{Comment, UserPost}
import net.liftweb.json.JsonAST.JObject
import controllers.APIException
import play.api.libs.json._
import play.api.libs.functional.syntax._
import net.liftweb.http.js.JE.JsFalse

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:44
 * To change this template use File | Settings | File Templates.
 */
object PostManager {

  val readLonLat : Reads[(Double,Double)] = {
    (JsPath \ "longitude").read[Double] and
    (JsPath \ "latitude").read[Double] tupled
  }

  def postNew(userId : Long, json : JsValue) = {

    val imageId = (json \ "imageId").as[Long]
    val goodness = (json \ "goodness").as[Int]
    val post = UserPost.create(userId,imageId,goodness)

    readLonLat.reads(json) match{
      case JsSuccess( (lon,lat) ,_ ) => {
        post.hasGpsInfo := true
        post.longitude := lon
        post.latitude := lat
        post.save()
      }
      case _ =>
    }

    post

  }

  def updatePost(userId : Long, postId : Long , updateInfo : JsValue) = {
    UserPost.findPost(userId,postId) match{
      case Some(post) => {
        updateParams(post,updateInfo)
        post
      }
      case None => {
        throw new APIException("Can't update not self post!")
      }
    }
  }


  def updateParams(post : UserPost, updateInfo : JsValue) {
    val updateInfoRead = (JsPath \ "comment").read[String] and
      (JsPath \ "category").read[Int] tupled

    (updateInfo \ "comment").asOpt[String].foreach(c => post.comment := c)
    (updateInfo \ "category").asOpt[Long].foreach(c => post.category := c)
    (updateInfo \ "goodness").asOpt[Int].foreach(c => post.goodness  := c)
    (updateInfo \ "title").asOpt[String].foreach(c => post.title := c)
    readLonLat.reads(updateInfo) match{
      case JsSuccess( (lon,lat) ,_ ) => {
        post.hasGpsInfo := true
        post.longitude := lon
        post.latitude := lat
      }
      case _ =>
    }
    post.save()


  }

  def commentTo(userId : Long , postId : Long , comment : String) = {
    val p = UserPost.findByKey(postId).get

    val c = Comment.create(p,userId,comment)


    c
  }

  def findNearPosts( lon : Double,lat : Double) = {

    UserPost.findNear(lon,lat,0.1)
  }

}
