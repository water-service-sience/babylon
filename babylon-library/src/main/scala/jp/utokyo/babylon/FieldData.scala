package jp.utokyo.babylon.db

import net.liftweb.mapper._

object FieldData extends FieldData with LongKeyedMetaMapper[FieldData]{


}
class FieldData extends LongKeyedMapper[FieldData] with IdPK{

  def getSingleton = FieldData

  object fieldRouterId extends MappedLong(this)
  object fieldDataType extends MappedString(this,128)
  object value extends MappedDouble(this)
  object updatedTime extends MappedDateTime(this)
}


