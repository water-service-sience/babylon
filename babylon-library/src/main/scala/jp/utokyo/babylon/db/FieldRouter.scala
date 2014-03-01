package jp.utokyo.babylon.db


import net.liftweb.mapper._

object FieldRouter extends FieldRouter with LongKeyedMetaMapper[FieldRouter]{


}
class FieldRouter extends LongKeyedMapper[FieldRouter] with IdPK{

  def getSingleton = FieldRouter

  object latitude extends MappedDouble(this)
  object longitude extends MappedDouble(this)
  object routerName extends MappedString(this,128){
    override def dbIndexed_? = true
  }
  object lastSyncTime extends MappedDateTime(this)
}