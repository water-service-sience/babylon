package web

import play.api.libs.ws.WS
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import org.slf4j.LoggerFactory
import jp.utokyo.babylon.db.FieldRouter

/**
 * Created by takezoux2 on 14/03/01.
 */
class MizoLabFieldRouter( id : String) {

  implicit val executionContext = ExecutionContext.global

  val logger = LoggerFactory.getLogger(classOf[MizoLabFieldRouter])


  def getLatestData() = {
    val csvs = getCsvNames
    val csv = selectLatestCsv(csvs)
    getTopDataOfCSV(urlForCsv(csv))
  }

  def selectLatestCsv(csvNames : List[String]) = {
    FieldRouter.findByName(id).toOption match{
      case Some(router) => {
        val sensorName = router.targetSensorName.get
        if(sensorName != null &&
          sensorName.length > 0){
          csvNames.find(_.contains(sensorName)).getOrElse(csvNames(0))
        }else{
          csvNames(0)
        }
      }
      case None => {
        csvNames(0)
      }
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

  def urlForDataList = {
    s"http://x-ability.jp/cgi-bin/FieldRouter/dirIndex.cgi?dir=/FieldRouter/${id}/data"
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
    else s"http://x-ability.jp${csvName}"
  }




}
