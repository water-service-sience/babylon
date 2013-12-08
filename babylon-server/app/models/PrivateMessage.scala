
package models

/**
 * Created by takezoux2 on 13/12/09.
 */

object PrivateMessage{

  implicit def toDBModel(post : PrivateMessage) = {post.dbModel}
  implicit def fromDbModel(dbModel : jp.utokyo.babylon.db.PrivateMessage) = PrivateMessage(dbModel)
  implicit def fromDbModelList(dbModels : List[jp.utokyo.babylon.db.PrivateMessage]) = dbModels.map(PrivateMessage(_))


}

case class PrivateMessage(dbModel : jp.utokyo.babylon.db.PrivateMessage) {
  
}
