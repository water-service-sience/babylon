package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:05
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object PostCategory extends PostCategory with LongKeyedMetaMapper[PostCategory]{


  def NoneCategory = {
    PostCategory.findByKey(1)
  }


}
class PostCategory extends LongKeyedMapper[PostCategory] with IdPK{

  def getSingleton = PostCategory

  object parent extends MappedLong(this){
    override def defaultValue = 0
  }
  object label extends MappedString(this,128)
}
