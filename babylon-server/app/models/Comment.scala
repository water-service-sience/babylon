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

object Comment extends Comment with LongKeyedMetaMapper[Comment]{

  def create(post : UserPost,userId : Long, comment : String) = {
    val c = createInstance
    c.userPost := post.id.is
    c.commentUser := userId
    c.comment := comment

    c.save()

    c
  }

  def getAllComments(postId : Long) = {
    findAll(By(Comment.userPost,postId),OrderBy(Comment.commented,Descending))
  }

}
class Comment extends LongKeyedMapper[Comment] with IdPK{

  def getSingleton = Comment

  object userPost extends MappedLongForeignKey(this,UserPost)
  object commentUser extends MappedLongForeignKey(this,User)
  object replyTo extends MappedLongForeignKey(this,Comment){
    override def defaultValue: Long = 0
  }
  object comment extends MappedText(this)
  object commented extends MappedDateTime(this){
    override def defaultValue = new Date()
  }
}
