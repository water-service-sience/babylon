package jp.utokyo.babylon.db

import net.liftweb.mapper._
import jp.utokyo.babylon.util.EncryptUtil
import java.util.Date
import jp.utokyo.babylon.annotations.JsonFieldLevel

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 2:47
 * To change this template use File | Settings | File Templates.
 */

object User extends User with LongKeyedMetaMapper[User]{

  def create(username : String, nickname : String) = {

    if(User.findByUsername(username).isDefined){
      throw new Exception("User:" + nickname + " already exists")
    }

    val u = User.createInstance

    u.nickname := nickname
    u.username := username
    u.accessKey := EncryptUtil.randomString(20)
    u.password := ""
    u.lastLogin := new Date
    u.save()


    //u.username := "user" + u.id.is + EncryptUtil.randomString(3)
    //u.save()

    u

  }

  def findByAccessKey(ak : String) = {
    User.find(By(User.accessKey,ak))
  }

  def findAllManagers() = {
    findAll(By(User.manager,true))
  }

  def findByUsername(username : String) = {
    User.find(By(User.username,username))
  }

  def searchUser(query : String) = {
    User.findAll(Like(User.username,"%" + query + "%")) :::
    User.findAll(Like(User.nickname,"%" + query + "%")) distinct
  }

  def setPassword(user : User,rawPassword : String) = {
    // 2度SHA256ハッシュをかける
    user.password := EncryptUtil.sha256(EncryptUtil.sha256(rawPassword))
    user.save()
  }
  def setPasswordFromClient(user : User, clientPassword : String) = {
    // クライアントから送られるパスワード１度ハッシュ済みなので、
    // 1度だけハッシュをかける
    user.password := EncryptUtil.sha256(clientPassword)
    user.save()
  }




}
class User extends LongKeyedMapper[User] with IdPK{

  def getSingleton = User

  object username extends MappedString(this,128)

  @JsonFieldLevel(1000)
  object password extends MappedString(this,128)
  object nickname extends MappedString(this,128)
  @JsonFieldLevel(100)
  object accessKey extends MappedString(this,128)
  object lastLogin extends MappedDateTime(this)

  @JsonFieldLevel(1000)
  object admin extends MappedBoolean(this)

  @JsonFieldLevel(1000)
  object manager extends MappedBoolean(this){
    override def dbIndexed_? : Boolean = true
  }

  @JsonFieldLevel(1000)
  object role extends MappedString(this,128)


  def emptyPassword_? = {
    password.get.size == 0
  }
  def correctPassword_?( passwordFromClient : String ) : Boolean = {
    //クライアントからは、1度だけsha256ハッシュがかけられたパスワードが送られるので、
    //もう一度だけsh256ハッシュをかけて比較
    if(this.password.get.length == 0){
      return true
    }

    val sha256 = EncryptUtil.sha256(passwordFromClient)

    password.get.size > 0 && (
      password.get == sha256 ||
      password.get == EncryptUtil.sha256(sha256) ||
      password.get == passwordFromClient
    )
  }

}