package jp.utokyo.babylon.db

import net.liftweb.mapper._

object DisplayFieldData extends DisplayFieldData with LongKeyedMetaMapper[DisplayFieldData]{
  def getDataListFor(routerId : Long) = {
    findAll(By(fieldRouterId,routerId))
  }

}
class DisplayFieldData extends LongKeyedMapper[DisplayFieldData] with IdPK{

  def getSingleton = DisplayFieldData

  object fieldRouterId extends MappedLong(this)
  object fieldDataType extends MappedString(this,128)
  object displayLabel extends MappedString(this,128)
}



