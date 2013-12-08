
package models

/**
 * Created by takezoux2 on 13/12/09.
 */

object UserPost{

  implicit def toDBModel(post : UserPost) = {post.dbModel}
  implicit def fromDbModel(dbModel : jp.utokyo.babylon.db.UserPost) = UserPost(dbModel)
  implicit def fromDbModelList(dbModels : List[jp.utokyo.babylon.db.UserPost]) = dbModels.map(UserPost(_))


}

case class UserPost(dbModel : jp.utokyo.babylon.db.UserPost) {

  def imageFileKey = dbModel.image.obj.map(_.fileKey.get).openOr("no_image.jpg")
  
}
