package controllers.management

import play.api.mvc.{AnyContent, Request, Action, Controller}
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
object TopPage extends ManagerBase {

  def getParam(key : String)(implicit req : Request[AnyContent]) = {
    req.getQueryString(key)
  }

  def searchInquiry = AdminAuth(implicit req => {


    Ok(views.html.index("Your new application is ready.",
      PostManager.getRecentInquiries(0,20,"")))
  })

  def getRecentPosts(start : Int = 0, count : Int = 20, q : String = "") = AdminAuth(req => {

    val posts = PostManager.getRecentInquiries(start,count,q)

    Ok(Json.arr(posts.map(p => {
      val o : JsValueWrapper = Json.obj(
        "id" -> p.id.is,
        "inCharge" -> (p.inCharge.map(_.nickname.get).getOrElse("No one")).toString,
        "category" -> p.category.map(_.label.get).getOrElse("no category").toString,
        "postStatus" -> p.postStatus.map(_.label.get).getOrElse("no status").toString,
        "user" -> Json.obj(
          "id" -> p.postUser.is,
          "name" -> {
            val s : String = p.postUser.obj.map(_.nickname.get).openOr("")
            s
          }

        )
      )
      o
    }) :_*))

  })

}
