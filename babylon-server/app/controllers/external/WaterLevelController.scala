package controllers.external

import play.api.mvc._
import jp.utokyo.babylon.db.{FieldRouter, WaterLevelField, WaterLevel}
import play.api.libs.json.{JsObject, Json, JsArray}
import java.text.SimpleDateFormat
import models.RouterInfo
import play.api.libs.json.Json.JsValueWrapper

/**
 * Created by takezoux2 on 2014/06/01.
 */
object WaterLevelController extends Controller {

  def image(routerId : Long) = Action{
    val r = FieldRouter.findByKey(routerId).get
    Ok(views.html.external.image(r.displayName,r.routerName))
  }
  def chart(span : String,routerId : Long) = Action{
    var index = 0
    val routers = WaterLevelField.findOfRouter(routerId).map(r => {
      index += 1
      RouterInfo(r.fieldRouter.obj.get.displayName.get + index,r.sensorName.get)
    })
    Ok(views.html.external.chart(span,routers))
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
      WaterLevel.getDataListInXHours(f,3)
    },"H:mm")

  def oneDay(routerId : Long) =
    fromDataList(routerId, f => {
      WaterLevel.getDataListInXDays(f,1)
    },"H:mm")
  def threeDays(routerId : Long) = {

    fromDataList(routerId, f => {
      WaterLevel.getDataListInXDays(f,3).grouped(6).map(_(0)).toList
    },"MM/dd H時")
  }

  def oneWeek(routerId : Long) = {

    fromDataList(routerId, f => {
      WaterLevel.getDataListInXDays(f,8).grouped(24).map(_(0)).toList
    },"MM/dd HH")
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
}
