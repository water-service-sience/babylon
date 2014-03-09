package jp.utokyo.babylon.util

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:33
 * To change this template use File | Settings | File Templates.
 */
import scala.util.Random
import java.security.MessageDigest

/**
 * Created with IntelliJ IDEA.
 * User: takezoux3
 * Date: 12/11/14
 * Time: 23:54
 * To change this template use File | Settings | File Templates.
 */
object EncryptUtil {


  def sha1( v : String) = {
    sha1Digest(v.getBytes("utf-8"))
  }
  def sha256(v : String) = {
    val data = v.getBytes("utf-8")

    val md = MessageDigest.getInstance("SHA-256");
    md.digest(data).map( b => "%02x".format(b)).mkString

  }

  def sha1Digest(data : Array[Byte]) = {
    val md = MessageDigest.getInstance("SHA-1");
    md.digest(data).map( b => "%02x".format(b)).mkString
  }


  val random = new Random
  val AlphabetAndNumbers = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toArray

  def randomString(length : Int) : String = {
    randomString(length,AlphabetAndNumbers)
  }

  def randomString(length : Int,characterList : Array[Char]) : String = {
    val b = new StringBuilder(length)
    val max = characterList.length
    for (i <- 0 until length){
      b.append(characterList(random.nextInt(max)))
    }
    b.toString()
  }
}
