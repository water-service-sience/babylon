package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:01
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object Comment extends Comment with LongKeyedMetaMapper[Comment]{


}
class Comment extends LongKeyedMapper[Comment] with IdPK{

  def getSingleton = Comment

  object userPost extends MappedLongForeignKey(this,UserPost)
  object commentUser extends MappedLongForeignKey(this,User)
  object replyTo extends MappedLongForeignKey(this,Comment)
  object comment extends MappedText(this)
  object commented extends MappedDateTime(this)
}
