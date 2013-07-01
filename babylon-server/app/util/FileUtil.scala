package util
import java.io.FileOutputStream

import java.io.File
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

}
