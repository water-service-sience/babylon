package controllers

import play.api.mvc.{Action, Controller}
import controllers.manager.Jsonize
import play.api.libs.json.JsArray
import jp.utokyo.babylon.db.UserPost

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/10/15
 * Time: 2:31
 * To change this template use File | Settings | File Templates.
 */
object PostList extends Controller{


  def nearPosts(longitude : Double, latitude : Double,range : Double,start : Int) = Action({

    val posts = UserPost.findNear(longitude,latitude,range,start)

    Ok(JsArray(posts.map(Jsonize.allInfo(_))))

  })

  def map = Action({


    Ok(views.html.event.map())
  })

}
