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

  def resizeTo(bytes : Array[Byte],extension : String, width : Int , height : Int) = {

    val image = ImageIO.read(new ByteArrayInputStream(bytes))

    val scaleX = width.toFloat / image.getWidth()
    val scaleY = height / image.getHeight

    val scaled = image.getScaledInstance(width,height,Image.SCALE_FAST)

    val buffered = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB)
    buffered.getGraphics.drawImage(scaled,0,0,null)

    val output = new ByteArrayOutputStream()
    ImageIO.write(buffered,extension,output)

    output.toByteArray
  }

}
