package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:01
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._
import java.util.Date
import net.liftweb.common.Full

object Land extends Land with LongKeyedMetaMapper[Land]{


  def findAllForUser(user : User) = {
    findAll(By(Land.user,user.id.get))
  }


  def updateLand(id : Long,user : User, name : String, latitude : Double,longitude : Double) = {

    findByKey(id) match{
      case Full(l) => {
        if(l.user.get == user.id.get){
          l.user := user.id.get
          l.name := name
          l.longitude := longitude
          l.latitude := latitude
          l.save()
        }

        l

      }
      case _ => {
        val l = Land.createInstance
        l.user := user.id.get
        l.name := name
        l.latitude := latitude
        l.longitude := longitude
        l.save()

        l
      }
    }

  }



}


class Land extends LongKeyedMapper[Land] with IdPK{

  def getSingleton = Land

  object user extends MappedLongForeignKey(this,User)
  object name extends MappedString(this,100)
  object longitude extends MappedDouble(this)
  object latitude extends MappedDouble(this)


}

