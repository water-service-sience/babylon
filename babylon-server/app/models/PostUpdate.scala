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

object PostUpdate extends PostUpdate with LongKeyedMetaMapper[PostUpdate]{

  def create(userPost : UserPost, editor : User) = {
    val pu = createInstance

    pu.editor := editor.id.get
    pu.userPost := userPost.id.get
    pu.comment := ""
    pu.replyTo := 0
    pu.edited := new Date

    pu
  }

  def findAllOf(up : UserPost) = {
    findAll(By(PostUpdate.userPost,up.id.get),OrderBy(PostUpdate.edited,Descending))
  }

}
class PostUpdate extends LongKeyedMapper[PostUpdate] with IdPK{

  def getSingleton = PostUpdate

  object userPost extends MappedLongForeignKey(this,UserPost)
  object editor extends MappedLongForeignKey(this,User)
  object comment extends MappedText(this)
  object replyTo extends MappedLongForeignKey(this,PostUpdate)
  object actionType extends MappedEnum(this,ActionType)
  object actionDetail extends MappedText(this)
  object edited extends MappedDateTime(this)
}


import net.liftweb.mapper._


object ActionType extends Enumeration{

  val ChangeInCharge = Value(1)
  val Comment = Value
  val ChangeStatus = Value
  val SendPrivateMessage = Value

}