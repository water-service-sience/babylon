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

  def postNew(userId : Long, imageId : Long, goodness : Int) = {

    val post = UserPost.create(userId,imageId,goodness)

    post

  }

  def updatePost(userId : Long, postId : Long , updateInfo : JsValue) = {
    UserPost.findPost(userId,postId) match{
      case Some(post) => {

        updateGps(post,updateInfo)
        updateParams(post,updateInfo)

        post
      }
      case None => {
        throw new APIException("Can't update not self post!")
      }
    }
  }

  def updateGps(post : UserPost , updateInfo : JsValue) {
    val lonLatRead : Reads[(Double,Double)] = (JsPath \ "longitude").read[Double] and
      (JsPath \ "latitude").read[Double] tupled

    lonLatRead.reads(updateInfo) match{
      case JsSuccess( (lon, lat), path) => {
        val image = post.image.obj.get
        image.hasGpsInfo := true
        image.longitude := lon
        image.latitude := lat
        image.save()
      }
      case JsError(errors) => {

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
    post.save()


  }

  def commentTo(userId : Long , postId : Long , comment : String) = {
    val p = UserPost.findByKey(postId).get

    val c = Comment.create(p,userId,comment)


    c
  }

}
