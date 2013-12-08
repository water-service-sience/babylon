
package models

/**
 * Created by takezoux2 on 13/12/09.
 */

object Contact{

  implicit def toDBModel(post : Contact) = {post.dbModel}
  implicit def fromDbModel(dbModel : jp.utokyo.babylon.db.Contact) = Contact(dbModel)
  implicit def fromDbModelList(dbModels : List[jp.utokyo.babylon.db.Contact]) = dbModels.map(Contact(_))


}

case class Contact(dbModel : jp.utokyo.babylon.db.Contact) {
  
}
