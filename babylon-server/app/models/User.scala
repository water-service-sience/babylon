package models
import net.liftweb.mapper._

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 2:47
 * To change this template use File | Settings | File Templates.
 */

object User extends User with LongKeyedMetaMapper[User]{

  def create(username : String , nickname : String, password : String) = {
    val u = User.createInstance
    u.nickname := nickname
    u.password := password
    u.username := username

    u.save()

    u

  }



}
class User extends LongKeyedMapper[User] with IdPK{

  def getSingleton = User

  object username extends MappedString(this,128)
  object password extends MappedString(this,128)
  object nickname extends MappedString(this,128)
  object lastLogin extends MappedDateTime(this)
  object role extends MappedString(this,128)

  def isAdmin = {
    role.is.split(",").contains("admin")
  }

}