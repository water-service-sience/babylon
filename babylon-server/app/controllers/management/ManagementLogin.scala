package controllers.management

import play.api.mvc.{Action, Controller}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/09/04
 * Time: 1:29
 * To change this template use File | Settings | File Templates.
 */
object ManagementLogin extends Controller {


  def login = Action({
    Ok("ok")
  })

  def logout = {
    Ok("ok")

  }
}
