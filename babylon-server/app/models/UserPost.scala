package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:00
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object UserPost extends UserPost with LongKeyedMetaMapper[UserPost]{


}
class UserPost extends LongKeyedMapper[UserPost] with IdPK{

  def getSingleton = UserPost

  object postUser extends MappedLongForeignKey(this,User)
  object title extends MappedString(this,128)
  object comment extends MappedText(this)
  object image extends MappedString(this,128)
  object goodness extends MappedLong(this)
  object posted extends MappedDateTime(this)
  object category extends MappedLongForeignKey(this,PostCategory)
  object userPostStatus extends MappedLongForeignKey(this,PostStatus)
  object inCharge extends MappedLongForeignKey(this,User)


}
