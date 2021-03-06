package controllers.api

import play.api.mvc.{Action, Controller}
import play.api.libs.json.{JsString, Json}
import jp.utokyo.babylon.db.User
import net.liftweb.common.{Empty, Full}

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
    val username = (json \ "username").as[String]
    val nickname = (json \ "nickname").as[String]
    //val password = (json \ "password").as[String]

    val u = User.create(username,nickname)

    Ok(Json.obj(
      "userId" -> u.id.is,
      "nickname" -> u.nickname.is,
      "username" -> u.username.is,
      "accessKey" -> u.accessKey.is
    ))
  })

  def login = Action(implicit req => {
    val json = req.body.asJson.get
    val JsString(username) = (json \ "username").get
    val password = (json \ "password").asOpt[String].getOrElse("")
    println(username + ":" + password)
    User.findByUsername(username) match{
      case Full(u) => {
        if(u.correctPassword_?(password)){
          Ok(Json.obj(
            "userId" -> u.id.is,
            "username" -> u.username.is,
            "nickname" -> u.nickname.is,
            "accessKey" -> u.accessKey.is
          ))
        }else{

          Ok(Json.obj(
            "result" -> 2,
            "message" -> "Wrong password or username"
          ))
        }
      }
      case _ => {


        Ok(Json.obj(
          "result" -> 2,
          "message" -> "Wrong password or username"
        ))
      }
    }


  })

}
