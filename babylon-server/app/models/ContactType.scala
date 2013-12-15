package models

/**
 * Created by takezoux2 on 13/12/16.
 */

object ContactType{

  implicit def toDBModel(post : ContactType) = {post.dbModel}
  implicit def fromDbModel(dbModel : jp.utokyo.babylon.db.ContactType) = ContactType(dbModel)
  implicit def fromDbModelList(dbModels : List[jp.utokyo.babylon.db.ContactType]) = dbModels.map(ContactType(_))


}

case class ContactType(dbModel : jp.utokyo.babylon.db.ContactType) {


}