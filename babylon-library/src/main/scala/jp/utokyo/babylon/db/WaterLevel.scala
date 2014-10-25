package jp.utokyo.babylon.db

import java.text.SimpleDateFormat
import java.util.Date
import net.liftweb.mapper._
import jp.utokyo.babylon.util.TimeUtil
import java.util.concurrent.TimeUnit

/**
  * Created by takezoux2 on 2014/06/01.
 */

object WaterLevel extends WaterLevel with LongKeyedMetaMapper[WaterLevel]{

  def replaceDate(f : WaterLevelField,values : List[(Date,Double)]) : Unit = {
    if(values.size == 0) return
    val oldest = new Date(values.map(_._1.getTime).min)
    bulkDelete_!!(By(waterLevelFieldId,f.id.get),By_>=(recordTime,oldest))
    // 45日以上前のデータも消す
    /*bulkDelete_!!(By(waterLevelFieldId,f.id.get),By_<(recordTime,
      new Date(new Date().getTime - TimeUnit.DAYS.toMillis(45))))*/
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

  def getLatestRecord(f : WaterLevelField) = {
    find(
      By(waterLevelFieldId,f.id.get),
      OrderBy(recordTime,Descending),MaxRows(1)).toOption
  }

  def getDataListInXHours(f : WaterLevelField,hours : Int,maxRecords : Int) = {
    getLatestRecord(f) match{
      case Some(r) => {
        val d = TimeUtil.beforeXHours(r.recordTime,hours)
        getDataListAfter(f,d,maxRecords)
      }
      case None => Nil
    }
  }
  def getDataListInXDays(f : WaterLevelField,days : Int,maxRecords : Int) = {
    getLatestRecord(f) match {
      case Some(r) => {
        val d = TimeUtil.beforeXDays(r.recordTime, days)
        getDataListAfter(f, d, maxRecords)
      }
      case None => Nil
    }
  }
  def getDataListAfter(f : WaterLevelField,pivot : Date,maxRecords : Int) = {
    // 一時的に、8月台の日付を強制的に取るようにする

    val diff = new Date().getTime - pivot.getTime
    val end = new SimpleDateFormat("yyyy-MM-dd").parse("2014-08-01")

    val start = new Date(end.getTime - diff)
    val l = findAll(
      By(waterLevelFieldId,f.id.get),
      By_>=(recordTime,start),
      By_<=(recordTime,end),
      OrderBy(recordTime,Descending)).reverse

    /*val l = findAll(
      By(waterLevelFieldId,f.id.get),
      By_>=(recordTime,pivot),
      OrderBy(recordTime,Descending)).reverse
    */
    val g = l.size / maxRecords
    if(g <= 1) l
    else {
      l.grouped(g).map(_.head).toList
    }
  }

  override def dbIndexes: List[BaseIndex[WaterLevel]] = Index(waterLevelFieldId,recordTime) :: Nil
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
