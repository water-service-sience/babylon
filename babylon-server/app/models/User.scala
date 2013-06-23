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