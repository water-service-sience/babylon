package controllers.api

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import jp.utokyo.babylon.db.User

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/25
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */
object AuthAPI extends Controller {


  def createAccount = Action(implicit req => {
    val json = req.body.asJson.get
    //val username = (json \ "username").as[String]
    val nickname = (json \ "nickname").as[String]
    //val password = (json \ "password").as[String]

    val u = User.create(nickname)

    Ok(Json.obj(
      "userId" -> u.id.is,
      "nickname" -> u.nickname.is,
      "accessKey" -> u.accessKey.is
    ))
  })



}
