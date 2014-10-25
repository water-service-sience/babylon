package web

import play.api.libs.ws.WS
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import org.slf4j.LoggerFactory
import jp.utokyo.babylon.db.{WaterLevelField, FieldRouter}
import java.util.Date
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

/**
 * Created by takezoux2 on 14/03/01.
 */
class MizoLabFieldRouter( id : String) {

  implicit val executionContext = ExecutionContext.global

  val logger = LoggerFactory.getLogger(classOf[MizoLabFieldRouter])


  def getWaterLevelsIn30Days(info : WaterLevelField) : List[(Date,Double)] = {
    val d = getDataFromCsvAfter(
      info,
      urlForFullCsv(info.sensorName.get),
      new Date(new Date().getTime - TimeUnit.DAYS.toMillis(30))
    )
    d
  }
  def getWaterLevelsInSummer(info : WaterLevelField) : List[(Date,Double)] = {
    val d = getDataFromCsvAfter(
      info,
      urlForFullCsv(info.sensorName.get),
      new SimpleDateFormat("yyyyMMdd").parse("20140601")
    )
    d
  }

  def getWaterLevels(info : WaterLevelField) : List[(Date,Double)] = {
    val csvs = getCsvNames
    val csv = selectLatestCsv(csvs,info.sensorName.get)
    val access = WS.url(urlForCsv(csv)).get().map(res => {
      val lines = res.body.lines
      // コメント行が入っているのをスキップ
      lines.next()
      // カラム名を取得
      val labels = lines.next().split(",").map(_.trim)
      val indexForSensor = labels.indexOf(info.sensorColumnName.get)
      val indexForTimestamp = labels.indexOf(info.timestampColumnName.get)

      if(indexForSensor < 0){
        logger.warn("Column for sensor not found|" + info.sensorColumnName.get +  " :" + labels.toList)
      }
      if(indexForTimestamp < 0){
        logger.warn("Column for timestamp not found|" + info.timestampColumnName.get + " :" + labels.toList)
      }
      // データ部分取得
      var data : List[(Date,Double)] = Nil
      val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm")
      while(lines.hasNext){
        val line = lines.next().split(",").map(_.trim)
        if(line.length > 0) {
          val sensorValue = (line(indexForSensor).toInt * info.valueFactor.get) + info.valueOffset.get
          data = (dateFormat.parse(line(indexForTimestamp)), sensorValue) :: data
        }
      }

      data

    })

    Await.result(access,1 minutes)


  }


  def getLatestData() = {
    val csvs = getCsvNames
    val csv = selectLatestCsv(csvs)
    getTopDataOfCSV(urlForCsv(csv))
  }

  def selectLatestCsv(csvNames : List[String]) : String = {
    FieldRouter.findByName(id).toOption match {
      case Some(router) => {
        val sensorName = router.targetSensorName.get
        selectLatestCsv(csvNames, sensorName)
      }
      case None => {
        csvNames(0)
      }
    }
  }
  def selectLatestCsv( csvNames : List[String],sensorName : String) : String = {

    if(sensorName != null &&
      sensorName.length > 0){
      csvNames.find(_.contains(sensorName)).getOrElse(csvNames(0))
    }else{
      csvNames(0)
    }
  }

  val csvNamesRegex = """\<a href='([^']+?\.csv)'>""".r
  def getCsvNames = {
    // aタグのリンクを全て取得し、不要なURLを取り除く
    val access = WS.url(urlForDataList).get().map(res => {
      logger.debug("Response:" + res.body)
      csvNamesRegex.findAllMatchIn(res.body).map( m => {
        m.group(1)
      }).filter(!_.contains("cgi-bin")).toList
    })

    Await.result(access,1 minutes)
  }

  def getDomain(routerName : String) = {

    def getId = if(routerName.toLowerCase.startsWith("vbox")){
      routerName.substring("vbox".length).stripPrefix("0").toInt
    }else 0

    if(getId > 100){
      "data01.x-ability.jp"
    }else{
      "x-ability.jp"
    }
  }
  def urlForDataList = {
    s"http://${getDomain(id)}/cgi-bin/FieldRouter/dirIndex.cgi?dir=/FieldRouter/${id}/data"
  }


  def getTopDataOfCSV( csvName : String) : List[(String,String)] = {
    val access = WS.url(urlForCsv(csvName)).get().map(res => {
      val lines = res.body.lines
      lines.next()
      val labels = lines.next().split(",").map(_.trim)
      var line : String = lines.next()
      while(lines.hasNext){
        line = lines.next()
      }

      labels.zip(line.split(",").map(_.trim)).toList

    })

    Await.result(access,1 minutes)
  }
  def urlForCsv(csvName : String) = {
    if(csvName.startsWith("http")) csvName
    else s"http://${getDomain(id)}${csvName}"
  }
  def urlForFullCsv(sensor : String) = {
    s"http://${getDomain(id)}/cgi-bin/FieldRouter/showDecagonCSV.cgi?dir=/FieldRouter/${id}/&device=${sensor}&full=full"
  }


  def getDataFromCsvAfter(info : WaterLevelField,csvUrl : String,timePivot : Date) : List[(Date,Double)] = {
    val access = WS.url(csvUrl).get().map(res => {
      val lines = res.body.lines
      // カラム名を取得
      val labels = lines.next().split(",").map(_.trim)
      var indexForSensor = labels.indexOf(info.dataCsvColumnName.get)
      val indexForTimestamp = labels.indexOf(info.timestampColumnName.get)

      if(indexForSensor < 0){
        indexForSensor = labels.indexOf("Millivolt[mV]")
        if(indexForSensor < 0) {
          logger.warn("Column for sensor not found|" + info.sensorColumnName.get + " :" + labels.toList)
        }
      }
      if(indexForTimestamp < 0){
        logger.warn("Column for timestamp not found|" + info.timestampColumnName.get + " :" + labels.toList)
      }
      // データ部分取得
      var data : List[(Date,Double)] = Nil
      val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm")
      while(lines.hasNext){
        val line = lines.next().split(",").map(_.trim)
        if(line.length > indexForSensor && line.length > indexForTimestamp) {
          val sensorValue = (line(indexForSensor).toDouble * info.valueFactor.get) + info.valueOffset.get
          data = (dateFormat.parse(line(indexForTimestamp)), sensorValue) :: data
        }
      }

      data

    })

    val dataList = Await.result(access,1 minutes)
    dataList.filter(p => p._1.after(timePivot)).sortBy(-_._1.getTime)
  }



}
