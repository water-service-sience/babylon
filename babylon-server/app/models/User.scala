
package models

/**
 * Created by takezoux2 on 13/12/09.
 */

object User{

  implicit def toDBModel(post : User) = {post.dbModel}
  implicit def fromDbModel(dbModel : jp.utokyo.babylon.db.User) = User(dbModel)
  implicit def fromDbModelList(dbModels : List[jp.utokyo.babylon.db.User]) = dbModels.map(User(_))


}

case class User(dbModel : jp.utokyo.babylon.db.User) {
  
}
