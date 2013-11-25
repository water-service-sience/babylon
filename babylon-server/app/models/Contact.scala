package models

/**
 * 電話番号などの登録
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:07
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object Contact extends Contact with LongKeyedMetaMapper[Contact]{

  def findAllFor(user : User) = {
    findAll(By(Contact.user,user.id.get))
  }

  def updateContacts(user : User, contacts : List[(String,String)]) = {

    Contact.bulkDelete_!!(By(Contact.user,user.id.get))

    val generated = contacts.map(c => {
      val co = Contact.createInstance

      co.user := user.id.get
      co.contactType := c._1
      co.contact := c._2
      co.save()

      co
    })

    generated
  }

  def createContact(userId : Long, contactType:Int,contact:  String) = {

    val co = Contact.createInstance
    co.user := userId
    co.contactType := contactType.toString
    co.contact := contact
    co.save()

    co


  }

}
class Contact extends LongKeyedMapper[Contact] with IdPK{

  def getSingleton = Contact

  object user extends MappedLongForeignKey(this,User)
  object contactType extends MappedString(this,128)
  object contact extends MappedString(this,128)
}
