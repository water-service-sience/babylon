package controllers.api

import play.api.mvc.Controller
import play.api.mvc.{AnyContent, Request}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/25
 * Time: 23:48
 * To change this template use File | Settings | File Templates.
 */
trait MyController extends Controller {

  def userId(implicit req : Request[AnyContent]) = {
    req.headers.get("USER-ID").map(_.toLong).get
  }

}
