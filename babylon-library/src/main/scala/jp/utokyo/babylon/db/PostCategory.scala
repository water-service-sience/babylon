package jp.utokyo.babylon.db

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:05
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._
import java.util.Date

object PostCategory extends PostCategory with LongKeyedMetaMapper[PostCategory]{

  def label(id : Long) = {
    findByKey(id).map(_.label.get).getOrElse("None")
  }

  def NoneCategory = {
    PostCategory.findByKey(1).get
  }

  def InquiryCategory = {
    PostCategory.findByKey(2).get
  }

  def createNewCategory(label : String) = {
    val c = createInstance
    c.label := label
    c.save()

    c
  }

}
class PostCategory extends LongKeyedMapper[PostCategory] with IdPK{

  def getSingleton = PostCategory

  object parent extends MappedLong(this){
    override def defaultValue = 0
  }
  object label extends MappedString(this,128)
}
