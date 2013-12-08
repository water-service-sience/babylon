package jp.utokyo.babylon.util

import net.liftweb.util.LRU
import net.liftweb.common.Full
import java.io.{FileNotFoundException, FileInputStream, FileOutputStream, File}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:29
 * To change this template use File | Settings | File Templates.
 */
object FileUtil {

  var cache = new LRU[String,Array[Byte]](30)


  def saveTo( path : String , bytes : Array[Byte]) = {
    val file = new File(path)
    if(!file.getParentFile().exists()){
      file.getParentFile().mkdirs();
    }

    val output = new FileOutputStream(path)
    output.write(bytes)
    output.close()
  }

  def getThumbnailKey(path : String) = {
    val i = path.lastIndexOf(".")
    if(i > 0){
      if( path.substring(0,i).endsWith("_s")){
        path
      }else{
        path.substring(0,i) + "_s" + path.substring(i)
      }
    }else{
      if(path.endsWith("_s")){
        path
      }else{
        path + "_s"
      }
    }
  }

  def readFrom(path : String) : Option[Array[Byte]] = {

    cache.get(path) match{
      case Full(data) => {
        Some(data)
      }
      case _ => {
        try{

          val input = new FileInputStream(path)
          val bytes = new Array[Byte](input.available())
          input.read(bytes)
          input.close()
          if(bytes.size < 10 * 1000 * 1000){
            cache.update(path,bytes)
          }
          Some(bytes)
        }catch{
          case e : FileNotFoundException => {
            None
          }
        }
      }
    }

  }

}
