package controllers.manager

import net.liftweb.json.JsonAST.JObject
import controllers.APIException
import play.api.libs.json._
import play.api.libs.functional.syntax._
import net.liftweb.http.js.JE.JsFalse
import java.util.{Date, Calendar}
import net.liftweb.mapper._
import play.api.libs.json.JsSuccess
import scala.Some
import jp.utokyo.babylon.db.{UserPost, Comment,PrivateMessage}
import net.liftweb.common.Full
import org.slf4j.LoggerFactory

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:44
 * To change this template use File | Settings | File Templates.
 */
object PostManager {

  val logger = LoggerFactory.getLogger(getClass)

  val readLonLat : Reads[(Double,Double)] = {
    (JsPath \ "longitude").read[Double] and
    (JsPath \ "latitude").read[Double] tupled
  }

  def postNew(userId : Long, json : JsValue) = {

    logger.debug(json.toString())
    val goodness = (json \ "goodness").asOpt[Int].getOrElse(0)
    val post = (json \ "isInquiry").asOpt[Boolean] match{
      case Some(true) => {
        (json \ "imageId").asOpt[Long] match{
          case Some(imageId) => {
            UserPost.create(userId,imageId,goodness)
          }
          case None => {
            UserPost.create(userId,0,goodness)
          }
        }
      }
      case _ => {
        (json \ "imageId").asOpt[Long] match{
          case Some(imageId) if imageId > 0 => {
            UserPost.create(userId,imageId,goodness)

          }
          case _ => {
            throw new Exception("Must attach image")
          }

        }
      }
    }


    (json \ "comment").asOpt[String].foreach( comment => {
      post.comment := comment
    })
    (json \ "category").asOpt[Int].foreach(category => {
      post.category := category
    })
    (json \ "isPublic").asOpt[Boolean].foreach(isPublic => {
      post.isPublic := isPublic
    })
    post.save()

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

  val ignoreFields = Set("id","updated","posted")
  val classToReads = Map[Class[_],Reads[_]](
    classOf[Int] -> Reads.IntReads,
    classOf[Long] -> Reads.LongReads,
    classOf[String] -> Reads.StringReads,
    classOf[Date] -> Reads.DefaultDateReads,
    classOf[Boolean] -> Reads.BooleanReads,
    classOf[Double] -> Reads.DoubleReads,
    classOf[Float] -> Reads.FloatReads
  )

  def updateParams(post : UserPost, updateInfo : JsValue) {

    (updateInfo.asInstanceOf[JsObject]).fields.foreach(p => {
      post.fieldByName(p._1) match{
        case Full(field) => {
          if(!ignoreFields.contains(field.name)) {
            val c = field.dbFieldClass
            classToReads.get(c) match{
              case Some(reads) => {
                (field.asInstanceOf[MappedField[Any,UserPost]]).set(
                  p._2.as[Any](reads.asInstanceOf[Reads[Any]]))
              }
              case None => {
                logger.debug(s"Unknown type for field:${field.name} @@ ${c.getName}")
              }
            }
          }
        }
        case _ => {
          logger.info(s"Unknown field for update:${p._1}")
        }
      }
    })
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

  def sendMessageTo(userId : Long, postId : Long, message : String) = {
    val p = UserPost.findByKey(postId).get

    val c = PrivateMessage.create(p,userId,message)

    c
  }


  def findNearPosts( lon : Double,lat : Double) = {

    UserPost.findNear(lon,lat,0.1)
  }

  def getRecentInquiries(start : Int, count : Int, q : String) = {
    UserPost.findRecentInquiries(start ,count , q)
  }

  def getOwnPost(userId : Long,_year : Int,_month : Int) = {
    val now = Calendar.getInstance()
    val year = if(_year == 0) now.get(Calendar.YEAR) else _year
    val month = if(_month == 0) now.get(Calendar.MONTH) else _month - 1
    val start = Calendar.getInstance()
    start.set(year,month,1)
    val end = Calendar.getInstance()
    end.set(year,month + 1,1)

    UserPost.findAll(By(UserPost.postUser,userId),
      By_>=(UserPost.posted,start.getTime),
      By_<=(UserPost.posted,end.getTime),
      OrderBy(UserPost.posted,Descending))
  }

  def getAllOwnPosts(userId : Long) = {
    UserPost.findAll(By(UserPost.postUser,userId),
      OrderBy(UserPost.posted,Descending))
  }

  def getPost(postId : Long) : UserPost = {

    UserPost.findByKey(postId).get
  }


}

case class SearchQuery(start : Int ,
                       count : Int,
                       query : Option[String]
                        )
