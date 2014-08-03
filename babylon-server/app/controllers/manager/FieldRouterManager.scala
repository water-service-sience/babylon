package controllers.manager

import jp.utokyo.babylon.db._
import web.MizoLabFieldRouter
import java.util.Date
import org.slf4j.LoggerFactory
import scala.Some
import java.util.concurrent.TimeUnit

/**
 * Created by takezoux2 on 2014/03/04.
 */
object FieldRouterManager {

  val updateSpan = TimeUnit.HOURS.toMillis(1) // １時間ごとに更新
  val logger = LoggerFactory.getLogger(getClass)

  def updateData() = {
    logger.debug("Begin update field router data")
    FieldRouter.findAll().foreach(fieldRouter => try{
      val timeDiff = new Date().getTime - fieldRouter.lastSyncTime.get.getTime
      if(timeDiff > updateSpan){
        logger.debug("Update " + fieldRouter.routerName.get)

        // 一週間程度以内の場合は、軽量な更新
        if(timeDiff < TimeUnit.DAYS.toMillis(7)){
          updateWaterLevel();

        }else{
          //1週間以上空いている場合は、フルに更新する
          updateWaterLevelIn30Days()
        }
        /*val v = new MizoLabFieldRouter(fieldRouter.routerName.get)
        val data = v.getLatestData()
        FieldRouter.updateData(fieldRouter,data)*/

        fieldRouter.lastSyncTime := new Date()
        fieldRouter.save()
      }
    }catch
    {
      case e : Throwable => {
        logger.error("Fail to update field router data:" + fieldRouter.id.get,e)
      }
    })
    logger.debug("Done update")


  }

  /**
   * 直近のデータだけを取得し、更新を行う
   * 比較的軽量
   */
  def updateWaterLevel() = {
    logger.debug("Light update")
    val waterLevelFields = WaterLevelField.findAll()
    logger.debug(waterLevelFields.size + " fields to update")
    waterLevelFields.foreach( f => {
      logger.debug("Update " + f.sensorColumnName.get)
      val fieldRouter = f.fieldRouter.obj.get
      val v = new MizoLabFieldRouter(fieldRouter.routerName.get)
      val values = v.getWaterLevels(f)

      WaterLevel.replaceDate(f,values)

    })

  }

  /**
   * 全てのCSVデータを取得して来て、そのうちの３０日以内のデータを更新する
   * 全データをダウンロードしてくるため、やや重い
   */
  def updateWaterLevelIn30Days() = {
    logger.info("Full update")
    val waterLevelFields = WaterLevelField.findAll()
    logger.debug(waterLevelFields.size + " fields to update")
    waterLevelFields.foreach( f => {
      logger.debug("Update " + f.sensorColumnName.get)
      val fieldRouter = f.fieldRouter.obj.get
      val v = new MizoLabFieldRouter(fieldRouter.routerName.get)
      val values = v.getWaterLevelsIn30Days(f)
      WaterLevel.replaceDate(f,values)

    })

  }

  def getLatestData(fieldRouterName : String) : List[(String,String)] = {

    getLatestData(FieldRouter.findByName(fieldRouterName).get)
  }

  def getLatestData(fieldRouter : FieldRouter) : List[(String,String)] = {
    val baseData = List(
      "場所" -> fieldRouter.displayName.get,
      "同期日時" -> fieldRouter.lastSyncTime.get.toString
    )
    val data = FieldData.getDataListFor(fieldRouter.id.get).map(d => d.fieldDataType.get.trim.toLowerCase -> d).toMap
    val displays = DisplayFieldData.getDataListFor(fieldRouter.id.get)
    baseData ++ displays.map(d => {
      data.get(d.fieldDataType.get.toLowerCase) match{
        case Some(v) => {
          d.displayLabel.get -> d.asString(v)
        }
        case None => d.displayLabel.get -> ""
      }
    })
  }

  def getNearestFieldData( latitude : Double,longitude : Double) = {
    val nearRouters = FieldRouter.findNearFieldRouters(latitude,longitude,20)

    nearRouters.sortBy(r => {
      val a = r.latitude.get - latitude
      val b = r.longitude.get - longitude
      a * a + b * b
    }).headOption match {
      case Some(router) => {
        getLatestData(router)
      }
      case None => {
        logger.warn("There are no field router.")
        val baseData = List(
          "場所" -> "NotFound"
        )
        baseData
      }
    }

  }
}
