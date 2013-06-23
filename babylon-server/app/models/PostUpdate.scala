package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:01
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object PostUpdate extends PostUpdate with LongKeyedMetaMapper[PostUpdate]{


}
class PostUpdate extends LongKeyedMapper[PostUpdate] with IdPK{

  def getSingleton = PostUpdate

  object userPost extends MappedLongForeignKey(this,UserPost)
  object editor extends MappedLongForeignKey(this,User)
  object comment extends MappedString(this,128)
  object edited extends MappedDateTime(this)
}

import net.liftweb.mapper._

object UpdateAction extends UpdateAction with LongKeyedMetaMapper[UpdateAction]{


}
class UpdateAction extends LongKeyedMapper[UpdateAction] with IdPK{

  def getSingleton = UpdateAction
  object postUpdate extends MappedLongForeignKey(this,PostUpdate)
  object actionType extends MappedEnum(this,ActionType)
  object longParam extends MappedLong(this)
  object stringParam extends MappedText(this)
}

object ActionType extends Enumeration{

  val ChangeInCharge = Value
  val Comment = Value
  val ChangeStatus = Value

}