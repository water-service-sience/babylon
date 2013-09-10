package models

import java.util.Date
import net.liftweb.common.Full

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:00
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object UserPost extends UserPost with LongKeyedMetaMapper[UserPost]{

  def create(userId : Long,imageId: Long,goodness : Int) = {
    val up = UserPost.createInstance
    up.postUser := userId
    up.image := imageId
    up.goodness := goodness
    up.save()

    up
  }

  def findPost(userId : Long, postId : Long) = {
    findByKey(postId) match{
      case Full(p) => {
        if(p.postUser.is == userId) Some(p)
        else None
      }
      case _ => None
    }
  }

  def findUserPosts(userId : Long) = {
    findAll(By(UserPost.postUser,userId),OrderBy(UserPost.posted,Descending))
  }

  def findNear(lon : Double,lat : Double, range : Double) = {

    findAll(By(UserPost.hasGpsInfo,true),
      By_<(UserPost.longitude,lon + range),
      By_>(UserPost.longitude,lon - range),
      By_<(UserPost.latitude,lat + range),
      By_>(UserPost.latitude,lat - range)
    )

  }

  def findRecentInquiries(start : Int, count : Int, q : String) = {
    if(q.length > 0){

    }else{
      //findAll(By(UserPost.category,PostCategory.InquiryCategory.id.is))
    }

    findAll(OrderBy(UserPost.updated,Descending))

  }


}
class UserPost extends LongKeyedMapper[UserPost] with IdPK{

  def getSingleton = UserPost

  object postUser extends MappedLongForeignKey(this,User)
  object title extends MappedString(this,128){
    override def defaultValue = ""
  }
  object comment extends MappedText(this){
    override def defaultValue: String = ""
  }
  object image extends MappedLongForeignKey(this,UploadedImage)
  object goodness extends MappedInt(this)
  object posted extends MappedDateTime(this)
  {
    override def defaultValue = new Date
  }
  object category extends MappedLongForeignKey(this,PostCategory){
    override def defaultValue: Long = 1

  }
  object postStatus extends MappedLongForeignKey(this,PostStatus) {
    override def defaultValue: Long = 1
  }
  object inCharge extends MappedLongForeignKey(this,User){
    override def defaultValue: Long = 0
  }
  object hasGpsInfo extends MappedBoolean(this){
    override def defaultValue: Boolean = false
  }
  object longitude extends MappedDouble(this){
    override def defaultValue: Double = 0.0
  }
  object latitude extends MappedDouble(this){
    override def defaultValue: Double = 0.0
  }

  object unreadComments extends MappedInt(this){
    override def  defaultValue : Int = 0
  }

  object unreadMessages extends MappedInt(this){
    override def defaultValue: Int = 0
  }

  object updated extends MappedDateTime(this){
    override def defaultValue = new Date
  }


}
