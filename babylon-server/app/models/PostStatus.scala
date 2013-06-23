package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:10
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object PostStatus extends PostStatus with LongKeyedMetaMapper[PostStatus]{


}
class PostStatus extends LongKeyedMapper[PostStatus] with IdPK{

  def getSingleton = PostStatus

  object label extends MappedString(this,128){
    override def defaultValue = "0"
  }
  object availableTransition extends MappedString(this,128)
}
