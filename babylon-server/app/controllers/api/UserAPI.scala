package controllers.api

import play.api.libs.json.{JsArray, JsString, Json}
import controllers.manager.Jsonize
import play.api.libs.json.Json.JsValueWrapper
import jp.utokyo.babylon.db.{User, Land, Contact}
import org.slf4j.LoggerFactory

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:12
 * To change this template use File | Settings | File Templates.
 */
object UserAPI extends MyController {

  val logger = LoggerFactory.getLogger(getClass)

  def updateLand = Authenticated(implicit req => {

    val body = req.body.asJson.get

    val id = (body \ "id").as[Long]
    val latitude = (body \ "latitude").as[Double]
    val longitude = (body \ "longitude").as[Double]
    val name = (body \ "name").as[String]

    val l = Land.updateLand(id,me,name,latitude,longitude)

    Ok(Jsonize.land(l))

  })

  def ownLands = Authenticated(implicit req => {

    val lands = Land.findAllForUser(me)

    Ok(Json.arr( lands.map( l => {
      Jsonize.land(l) : JsValueWrapper
    }) :_* ))
  })

  def getContacts = Authenticated(implicit req => {
    val contacts = Contact.findAllFor(me)

    Ok(Json.arr(
      contacts.map(Jsonize.contact(_) : JsValueWrapper) :_*
    ))

  })

  def updateContacts = Authenticated(implicit req => {
    val body = req.body.asJson.get
    //println(body)
    //println( (body \\ "contacts"))

    val JsArray(contactList) = body \ "contacts"

    val contacts = contactList.map( e => {
      (e \ "contactType").as[Long] ->
        (e \ "contact").as[String]
    }).toList


    //println( (body \\ "contacts"))

    val c = Contact.updateContacts(me,contacts).toList

    //println( "####")
    Ok(JsArray(c.map(Jsonize.contact(_))))


  })

  def resetPassword = Authenticated(implicit req => {
    val json = req.body.asJson.get
    val nickname = (json \ "nickname").asOpt[String]
    val username = (json \ "username").asOpt[String]
    val JsString(newPassword) = (json \ "password")
    //val JsString(oldPassword) = (json \ "oldPassword")
    val u = me


    if(newPassword.size < 4) {
      logger.info("Password is too short")
      Ok(Json.obj(
        "result" -> 2,
        "message" -> "パスワードが短すぎます"
      ))
    }else if(username.map(_.length > 0).getOrElse(false) && u.username.get != username.get &&
      User.findByUsername(username.get).isDefined){
      logger.info("Duplicate nickname")
      Ok(Json.obj(
        "result" -> 3,
        "message" -> "既に存在するユーザー名です。"
      ))
    }else{//} if(u.emptyPassword_? || u.correctPassword_?(oldPassword)){
      logger.debug("Change password")
      User.setPasswordFromClient(u,newPassword)
      if(username.map(_.length > 0).getOrElse(false)) {
        logger.debug("Change username")
        u.username := username.get
      }
      nickname.map(u.nickname := _)
      u.save()
      Ok(Json.obj(
        "result" -> 1
      ))
    }/*else{
      logger.info("Wrong password")
      Ok(Json.obj(
        "result" -> 4,
        "message" -> "パスワードが間違っています。"
      ))
    }*/

  })





}
