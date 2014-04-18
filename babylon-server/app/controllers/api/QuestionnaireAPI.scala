package controllers.api

import play.api.mvc.Action
import controllers.manager.{Jsonize, PostManager, PhotoManager}
import play.api.libs.json.Json
import play.api.libs.json.Json.JsValueWrapper
import play.api.Logger
import jp.utokyo.babylon.db._

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:10
 * To change this template use File | Settings | File Templates.
 */
object QuestionnaireAPI extends MyController{

  def answer = Authenticated( implicit req => {
    val json = req.body.asJson.get
    println(json)
    val eval = (json \ "evaluation").as[String].toInt
    val note = (json \ "note").asOpt[String].getOrElse("")
    println("aaaa")

    val p = QuestionnaireAnswer.createOrUpdate(userId,eval,note)

    Ok(Jsonize.withMessage("ok"))

  })

}
