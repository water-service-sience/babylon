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
  object userPostStatus extends MappedLongForeignKey(this,PostStatus) {
    override def defaultValue: Long = 1
  }
  object inCharge extends MappedLongForeignKey(this,User){
    override def defaultValue: Long = 0
  }


}
