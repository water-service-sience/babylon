package controllers.api

import play.api.libs.json.{JsString, Json}
import controllers.manager.Jsonize
import play.api.libs.json.Json.JsValueWrapper
import jp.utokyo.babylon.db.{User, Land, Contact}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:12
 * To change this template use File | Settings | File Templates.
 */
object UserAPI extends MyController {


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

    val contacts = (body \\ "contacts").map( e => {
      (e \ "contactType").as[Long] ->
        (e \ "contact").as[String]
    }).toList


    val c = Contact.updateContacts(me,contacts).toList

    Ok(Json.arr(c.map(Jsonize.contact(_) : JsValueWrapper) :_* ))


  })

  def resetPassword = Authenticated(implicit req => {
    val json = req.body.asJson.get
    val JsString(newPassword) = (json \ "newPassword")
    val JsString(confirmPassword) = (json \ "confirmPassword")
    val JsString(oldPassword) = (json \ "oldPassword")
    val u = me

    if(newPassword.size < 4){
      InternalServerError(Json.obj(
        "result" -> 2,
        "message" -> "Password is too short"
      ))
    }else if(newPassword != confirmPassword){
      InternalServerError(Json.obj(
        "result" -> 3,
        "message" -> "Not match confirm password"
      ))
    }else if(u.correctPassword_?(oldPassword)){
      User.setPasswordFromClient(u,newPassword)
      Ok(Json.obj(
        "result" -> 1
      ))
    }else{
      InternalServerError(Json.obj(
        "result" -> 4,
        "message" -> "Wrong password"
      ))
    }

  })





}
