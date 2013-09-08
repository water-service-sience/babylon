package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:01
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._
import java.util.Date

object PrivateMessage extends PrivateMessage with LongKeyedMetaMapper[PrivateMessage]{

  def create(post : UserPost,userId : Long, comment : String) = {
    val c = createInstance
    c.userPost := post.id.is
    c.commentUser := userId
    c.comment := comment

    c.save()

    c
  }

  def getAllAdminComments(postId : Long) = {
    findAll(By(PrivateMessage.userPost,postId),OrderBy(PrivateMessage.commented,Descending))
  }

}
class PrivateMessage extends LongKeyedMapper[PrivateMessage] with IdPK{

  def getSingleton = PrivateMessage

  object userPost extends MappedLongForeignKey(this,UserPost)
  object commentUser extends MappedLongForeignKey(this,User)
  object comment extends MappedText(this)
  object commented extends MappedDateTime(this){
    override def defaultValue = new Date()
  }
}
