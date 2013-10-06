package controllers.api

import models.{Contact, Land}
import play.api.libs.json.Json
import controllers.manager.Jsonize
import play.api.libs.json.Json.JsValueWrapper

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
      (e \ "contactType").as[String] ->
        (e \ "contact").as[String]
    }).toList


    val c = Contact.updateContacts(me,contacts).toList

    Ok(Json.arr(c.map(Jsonize.contact(_) : JsValueWrapper) :_* ))


  })

}
