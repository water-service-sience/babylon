package models
import net.liftweb.mapper._
import util.EncryptUtil
import java.util.Date

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 2:47
 * To change this template use File | Settings | File Templates.
 */

object User extends User with LongKeyedMetaMapper[User]{

  def create( nickname : String) = {
    val u = User.createInstance

    u.nickname := nickname
    u.accessKey := EncryptUtil.randomString(20)
    u.username := EncryptUtil.randomString(3)
    u.password := EncryptUtil.randomString(8)
    u.lastLogin := new Date
    u.save()


    u.username := "user" + u.id.is + EncryptUtil.randomString(3)
    u.save()

    u

  }

  def findByAccessKey(ak : String) = {

    User.find(By(User.accessKey,ak))

  }

  def findAllManagers() = {
    findAll(By(User.manager,true))
  }

  def findByUsername(username : String) = {
    User.find(By(User.username,username))
  }

  def searchUser(query : String) = {
    User.findAll(Like(User.username,"%" + query + "%"))
  }


}
class User extends LongKeyedMapper[User] with IdPK{

  def getSingleton = User

  object username extends MappedString(this,128)
  object password extends MappedString(this,128)
  object nickname extends MappedString(this,128)
  object accessKey extends MappedString(this,128)
  object lastLogin extends MappedDateTime(this)

  object admin extends MappedBoolean(this)
  object manager extends MappedBoolean(this){
    override def dbIndexed_? : Boolean = true
  }

  object role extends MappedString(this,128)

}