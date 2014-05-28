package jp.utokyo.babylon.db

import net.liftweb.mapper._

object FieldData extends FieldData with LongKeyedMetaMapper[FieldData]{
  def getDataListFor(routerId : Long) = {
    findAll(By(fieldRouterId,routerId))
  }

}
class FieldData extends LongKeyedMapper[FieldData] with IdPK{

  def getSingleton = FieldData

  object fieldRouterId extends MappedLong(this)
  object fieldDataType extends MappedString(this,128)
  object value extends MappedString(this,256)
  object updatedTime extends MappedDateTime(this)


}


