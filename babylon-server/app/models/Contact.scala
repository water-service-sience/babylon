package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:07
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object Contact extends Contact with LongKeyedMetaMapper[Contact]{


}
class Contact extends LongKeyedMapper[Contact] with IdPK{

  def getSingleton = Contact

  object user extends MappedLongForeignKey(this,User)
  object contactType extends MappedString(this,128)
  object contact extends MappedString(this,128)
}
