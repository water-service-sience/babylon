package controllers.management

import play.api.mvc.{Action, Controller}
import controllers.manager.PostManager
import play.api.libs.json.Json
import play.api.libs.json.Json.JsValueWrapper

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/07/16
 * Time: 1:48
 * To change this template use File | Settings | File Templates.
 */
object TopPage extends Controller {

  def getRecentPosts(start : Int = 0, count : Int = 20, q : String = "") = Action({

    val posts = PostManager.getRecentInquiries(start,count,q)


    Ok(Json.arr(posts.map(p => {
      val o : JsValueWrapper = Json.obj(
        "id" -> p.id.is,
        "title" -> p.title.is,
        "user" -> Json.obj(
          "id" -> p.postUser.is,
          "name" -> {
            val s : String = p.postUser.obj.map(_.nickname.get).openOr("")
            s
          }

        ),
        "category" -> Json.obj(
          "id" -> p.category.is,
          "name" -> {
            val s : String = p.category.map(_.label.get).openOr("")
            s
          }
        )
      )
      o
    }) :_*))

  })

}
