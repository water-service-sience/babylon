package util
import java.io.{FileInputStream, FileOutputStream, File}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:29
 * To change this template use File | Settings | File Templates.
 */
object FileUtil {

  def saveTo( path : String , bytes : Array[Byte]) = {
    val file = new File(path)
    if(!file.getParentFile().exists()){
      file.getParentFile().mkdirs();
    }

    val output = new FileOutputStream(path)
    output.write(bytes)
    output.close()
  }

  def readFrom(path : String) = {
    val input = new FileInputStream(path)
    val bytes = new Array[Byte](input.available())
    input.read(bytes)
    input.close()
    bytes
  }

}
