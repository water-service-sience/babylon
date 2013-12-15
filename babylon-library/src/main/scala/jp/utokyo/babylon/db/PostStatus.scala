package jp.utokyo.babylon.db

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:10
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._
import java.util.Date

object PostStatus extends PostStatus with LongKeyedMetaMapper[PostStatus]{

  def createNewStatus(label : String) = {
    val i = createInstance
    i.label := label
    i.save()
    i
  }

}
class PostStatus extends LongKeyedMapper[PostStatus] with IdPK{

  def getSingleton = PostStatus

  object label extends MappedString(this,128){
    override def defaultValue = "0"
  }
  object availableTransition extends MappedString(this,128)
}
