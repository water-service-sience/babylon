package jp.utokyo.babylon.db

import net.liftweb.mapper._

object FieldDataJSON extends FieldDataJSON with LongKeyedMetaMapper[FieldDataJSON]{
  def getData(routerId : Long) = {
    find(By(fieldRouterId,routerId))
  }

}

class FieldDataJSON extends LongKeyedMapper[FieldDataJSON] with IdPK{

  def getSingleton = FieldData

  object fieldRouterId extends MappedLong(this)
  object jsonData extends MappedText(this)
  object updatedTime extends MappedDateTime(this)


}


