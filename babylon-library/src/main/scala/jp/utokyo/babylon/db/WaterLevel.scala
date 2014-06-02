package jp.utokyo.babylon.db

import java.util.Date
import net.liftweb.mapper._
/**
  * Created by takezoux2 on 2014/06/01.
 */

object WaterLevel extends WaterLevel with LongKeyedMetaMapper[WaterLevel]{

  def replaceDate(f : WaterLevelField,values : List[(Date,Double)]) = {

    val oldest = new Date(values.map(_._1.getTime).min)
    bulkDelete_!!(By(waterLevelFieldId,f.id.get),By_>=(recordTime,oldest))
    // 10日以上前のデータも消す
    bulkDelete_!!(By(waterLevelFieldId,f.id.get),By_<(recordTime,
      new Date(new Date().getTime - 10 * 24 * 60 * 60 * 1000)))
    values.foreach(v => {
      val wl = createInstance
      wl.waterLevelFieldId := f.id.get
      wl.level := v._2
      wl.recordTime := v._1
      wl.save
    })
  }

  def getDataList(f : WaterLevelField) = {
    findAll(By(waterLevelFieldId,f.id.get),OrderBy(recordTime,Descending))
  }

  def getDataListInXHours(f : WaterLevelField,hours : Int) = {
    findAll(
      By(waterLevelFieldId,f.id.get),
      OrderBy(recordTime,Descending),
      MaxRows(hours * 6)).reverse

  }
  def getDataListInXDays(f : WaterLevelField,days : Int) = {
    findAll(
      By(waterLevelFieldId,f.id.get),
      OrderBy(recordTime,Descending),
      MaxRows(days * 6 * 24)).grouped(6).map(_(0)).toList.reverse

  }

}
class WaterLevel extends LongKeyedMapper[WaterLevel] with IdPK{

  def getSingleton = WaterLevel

  object waterLevelFieldId extends MappedLong(this)
  object level extends MappedDouble(this){
    override def defaultValue = 0
  }
  object recordTime extends MappedDateTime(this){
    override def defaultValue = new Date
  }
}
