package controllers.management

import jp.utokyo.babylon.db.PostStatus
import jp.utokyo.babylon.util.Jsonizer

/**
 * Created by takezoux2 on 13/12/09.
 */
object EditPostStatus extends ManagerBase {


  def list = AdminAuth(req => {

    val p = PostStatus.findAll()

    Jsonizer.listToJson(p)

  })


}
