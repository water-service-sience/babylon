package controllers.manager

import jp.utokyo.babylon.db.{DisplayFieldData, FieldData, FieldRouter}
import web.MizoLabFieldRouter
import java.util.Date
import org.slf4j.LoggerFactory

/**
 * Created by takezoux2 on 2014/03/04.
 */
object FieldRouterManager {

  val updateSpan = 60 * 60 * 1000 // １時間ごとに更新
  val logger = LoggerFactory.getLogger(getClass)

  def updateData = {
    logger.debug("Begin update field router data")
    FieldRouter.findAll().foreach(fieldRouter => try{
      if(new Date().getTime - fieldRouter.lastSyncTime.get.getTime > updateSpan){
        logger.debug("Update " + fieldRouter.routerName.get)
        val v = new MizoLabFieldRouter(fieldRouter.routerName.get)
        val data = v.getLatestData()
        FieldRouter.updateData(fieldRouter,data)
      }
    }catch
    {
      case e : Throwable => {
        logger.error("Fail to update field router data:" + fieldRouter.id.get,e)
      }
    })
    logger.debug("Done update")


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
        case Some(v) => d.displayLabel.get -> v.value.get
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
