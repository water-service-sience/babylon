
package models

/**
 * Created by takezoux2 on 13/12/09.
 */

object PostCategory{

  implicit def toDBModel(post : PostCategory) = {post.dbModel}
  implicit def fromDbModel(dbModel : jp.utokyo.babylon.db.PostCategory) = PostCategory(dbModel)
  implicit def fromDbModelList(dbModels : List[jp.utokyo.babylon.db.PostCategory]) = dbModels.map(PostCategory(_))


}

case class PostCategory(dbModel : jp.utokyo.babylon.db.PostCategory) {
  
}
