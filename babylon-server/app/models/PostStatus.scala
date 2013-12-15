
package models

import jp.utokyo.babylon.db.{PostStatus => MPostStatus}

/**
 * Created by takezoux2 on 13/12/09.
 */

object PostStatus{

  implicit def toDBModel(post : PostStatus) = {post.dbModel}
  implicit def fromDbModel(dbModel : jp.utokyo.babylon.db.PostStatus) = PostStatus(dbModel)
  implicit def fromDbModelList(dbModels : List[jp.utokyo.babylon.db.PostStatus]) = dbModels.map(PostStatus(_))


}
case class PostStatus(dbModel : MPostStatus)

