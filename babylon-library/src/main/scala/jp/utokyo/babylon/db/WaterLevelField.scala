package jp.utokyo.babylon.db

/**
 * Created by takezoux2 on 2014/06/01.
 */
import net.liftweb.mapper._

object WaterLevelField extends WaterLevelField with LongKeyedMetaMapper[WaterLevelField]{

  def findOfRouter(fieldRouterId : Long) = {
    findAll(By(fieldRouter,fieldRouterId))
  }
}
class WaterLevelField extends LongKeyedMapper[WaterLevelField] with IdPK{

  def getSingleton = WaterLevelField

  object fieldRouter extends MappedLongForeignKey(this,FieldRouter)
  object sensorName extends MappedString(this,128)
  object displayName extends MappedString(this,128)
  object timestampColumnName extends MappedString(this,128)
  object sensorColumnName extends MappedString(this,128)
  object dataCsvColumnName extends MappedString(this,128)
  object valueFactor extends MappedDouble(this)
  object valueOffset extends MappedDouble(this)
}