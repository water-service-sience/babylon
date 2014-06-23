package controllers.external

import play.api.mvc._
import jp.utokyo.babylon.db.{FieldRouter, WaterLevelField, WaterLevel}
import play.api.libs.json.{JsObject, Json, JsArray}
import java.text.SimpleDateFormat
import models.RouterInfo
import play.api.libs.json.Json.JsValueWrapper
import net.liftweb.common.Full

/**
 * Created by takezoux2 on 2014/06/01.
 */
object WaterLevelController extends Controller {

  def routers = FieldRouter.findAll.map(r => {
    r.id.get -> r.displayName.get
  })

  def chart(span : String,routerId : Long) = Action{
    var index = 0
    val routers = WaterLevelField.findOfRouter(routerId).map(r => {
      index += 1
      RouterInfo(r.fieldRouter.obj.get.displayName.get + "_" + index,r.sensorName.get)
    })
    Ok(views.html.external.chart(routerId,span,routers,this.routers))
  }

  def fromDataList(routerId : Long,getDataList : (WaterLevelField => List[WaterLevel]),_dateFormat : String) = Action{

    val dateFormat = new SimpleDateFormat(_dateFormat)

    val allData = WaterLevelField.findOfRouter(routerId).map(f => {

      val dataList = getDataList(f)
      val data = Json.toJson(dataList.map(d => {
        d.level.get
      }))
      val labels = Json.toJson(dataList.map(d => {
        dateFormat.format(d.recordTime.get)
      }))
      f.sensorName.get ->Json.obj(
        "labels" -> labels,
        "datasets" -> JsArray(List(baseJson + ("data" -> data)))
      )
    })
    Ok(JsObject(allData))
  }

  def threeHours(routerId : Long) =
    fromDataList(routerId, f => {
      WaterLevel.getDataListInXHours(f,3,6)
    },"H:mm")

  def oneDay(routerId : Long) =
    fromDataList(routerId, f => {
      WaterLevel.getDataListInXDays(f,1,6)
    },"H:mm")

  def threeDays(routerId : Long) = {
    fromDataList(routerId, f => {
      WaterLevel.getDataListInXDays(f,3,6)
    },"d日H時")
  }

  def oneWeek(routerId : Long) = {
    fromDataList(routerId, f => {
      WaterLevel.getDataListInXDays(f,8,16)
    },"d日H時")
  }

  val baseJson = Json.parse(
    """
      |{
      |    "fillColor" : "#afeeee",
      |    "strokeColor" : "rgba(220,220,220,1)",
      |    "pointColor" : "rgba(220,220,220,1)",
      |    "pointStrokeColor" : "#fff"
      |}
    """.stripMargin).asInstanceOf[JsObject]

  def waterLevel(routerId : Long) = {

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
  def image(routerId : Long) = Action{
    val r = FieldRouter.findByKey(routerId).get
    val url = s"http://${getDomain(r.routerName.get)}/cgi-bin/FieldRouter/latest.cgi?dir=/FieldRouter/${r.routerName.get}&target=image0"
    Ok(views.html.external.image(routerId, r.displayName.get,url,routers))
  }

  def calendar(routerId : Long) = Action{

    FieldRouter.findByKey(routerId) match{
      case Full(router) => {
        val url = s"http://${getDomain(router.routerName.get)}/cgi-bin/FieldRouter/imageIndex.cgi?dir=/FieldRouter/${router.routerName.get}&device=image0&addr=webcam0"

        Ok(views.html.external.calendar(routerId,url,routers))
      }
      case _ => NotFound
    }

  }
  def weatherData(routerId : Long) = Action{
    FieldRouter.findByKey(routerId) match{
      case Full(router) => {

        val url = s"http://${getDomain(router.routerName.get)}/cgi-bin/FieldRouter/showDecagon.cgi?dir=/FieldRouter/${router.routerName.get}&device=${router.targetSensorName.get}"

        Ok(views.html.external.weather_data(routerId,url,routers))
      }
      case _ => NotFound
    }

  }
}
