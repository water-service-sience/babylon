package jp.utokyo.babylon.db


import net.liftweb.mapper._
import java.util.Date

object FieldRouter extends FieldRouter with LongKeyedMetaMapper[FieldRouter]{

  def findNearFieldRouters(latitude : Double,longitude : Double, range : Double) = {
    findAll(
      By_>(this.latitude,latitude - range),By_<(this.latitude,latitude + range),
      By_>(this.longitude,longitude - range),By_<(this.longitude,longitude + range)
    )
  }

  def findByName( routerName : String ) = {
    find(By(FieldRouter.routerName,routerName))
  }

  def updateData(router : FieldRouter, data : Seq[(String,String)]) = {

    val dataMap = FieldData.getDataListFor(router.id.get).map(m => m.fieldDataType.get -> m).toMap

    data.map({
      case (k,v) => dataMap.get(k) match{
        case Some(d) => {
          d.value := v
          d.updatedTime := new Date
          d.save
          d
        }
        case None => {
          val d = FieldData.createInstance
          d.fieldRouterId := router.id.get
          d.fieldDataType := k
          d.value := v
          d.updatedTime := new Date()
          d.save()
          d
        }
      }
    })


    router.lastSyncTime := new Date()
    router.save()

  }


}
class FieldRouter extends LongKeyedMapper[FieldRouter] with IdPK{

  def getSingleton = FieldRouter

  object latitude extends MappedDouble(this)
  object longitude extends MappedDouble(this)
  object routerName extends MappedString(this,128){
    override def dbIndexed_? = true
  }
  object lastSyncTime extends MappedDateTime(this)
  object displayName extends MappedString(this,128)
  object targetSensorName extends MappedString(this,128)


}