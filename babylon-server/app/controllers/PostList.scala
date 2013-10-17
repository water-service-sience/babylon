package controllers

import play.api.mvc.{Action, Controller}
import models.UserPost
import controllers.manager.Jsonize
import play.api.libs.json.JsArray

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/10/15
 * Time: 2:31
 * To change this template use File | Settings | File Templates.
 */
object PostList extends Controller{


  def nearPosts(categoryIds : String,longitude : Double, latitude : Double,range : Double,start : Int) = Action({

    val catIds = categoryIds.split(",").map(_.toLong).toList
    println("$$$" + categoryIds)
    val posts = UserPost.findNear(catIds,longitude,latitude,range,start)

    Ok(JsArray(posts.map(Jsonize.allInfo(_))))

  })

  def map = Action({


    Ok(views.html.event.map())
  })

}
