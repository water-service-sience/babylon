package util

import java.awt.Image
import javax.imageio.ImageIO
import java.io.{ByteArrayOutputStream, ByteArrayInputStream}
import java.awt.image.BufferedImage

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/10/14
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
object ImageUtil {

  def resizeTo(bytes : Array[Byte],extension : String, width : Int , _height : Int) = {

    val image = ImageIO.read(new ByteArrayInputStream(bytes))
    val height = if(_height > 0){
      _height
    }else{
      val scale = width.toFloat / image.getWidth()
      (_height * scale).toInt
    }
    val scaled = image.getScaledInstance(width,height,Image.SCALE_FAST)

    val buffered = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB)
    buffered.getGraphics.drawImage(scaled,0,0,null)

    val output = new ByteArrayOutputStream()
    ImageIO.write(buffered,extension,output)

    output.toByteArray
  }

}
