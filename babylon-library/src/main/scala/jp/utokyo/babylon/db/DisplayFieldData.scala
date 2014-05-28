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

  object isDouble extends MappedBoolean(this){
    override def defaultValue: Boolean = true
  }
  object valueOffset extends MappedString(this,128){
    override def defaultValue: String = "0"
  }
  object valueFactor extends MappedString(this,128){
    override def defaultValue : String = "1.0"
  }
  object valueUnit extends MappedString(this,128) {
    override def defaultValue : String = ""
  }

  def asDoubleValue(data : FieldData) = {
    data.value.get.toDouble * valueFactor.get.toDouble + valueOffset.get.toDouble
  }

  def asString(data : FieldData) : String = {
    if(isDouble.get){
      asDoubleValue(data) + valueUnit.get
    }else{
      data.value.get + valueUnit.get
    }
  }

}



