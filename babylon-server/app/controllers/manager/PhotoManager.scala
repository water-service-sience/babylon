package controllers.manager

import play.api.mvc.{RawBuffer, AnyContent, Request}
import controllers.APIException
import play.api.{Logger, Configuration}
import jp.utokyo.babylon.db.UploadedImage
import jp.utokyo.babylon.util.{ImageUtil, FileUtil, EncryptUtil}
import java.io.FileInputStream

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:22
 * To change this template use File | Settings | File Templates.
 */
object PhotoManager {

  var imageDir = "./images/"

  def saveUploadedFile(userId : Long)(implicit req : Request[RawBuffer]) = {

    val buffer = getBytesFromRawBuffer(req.body)

    Logger.debug("User:%s uploaded image.Size = %s".format(userId,buffer.size))
    val b = buffer
    val filename = "u" + userId + "/" + EncryptUtil.sha1Digest(b)
    FileUtil.saveTo(imageDir + filename,b)
    val thumb = ImageUtil.resizeTo(b,"JPG",240,-1)
    FileUtil.saveTo(imageDir + filename + "_s",thumb)

    UploadedImage.create(userId,filename)
  }

  def getThumbnailData(fileKey : String) = {
    val b = FileUtil.readFrom(imageDir + FileUtil.getThumbnailKey(fileKey)) orElse
      FileUtil.readFrom(imageDir + fileKey)

    b.get

  }

  def getFileData(fileKey : String) = {

    val b = FileUtil.readFrom(imageDir + fileKey)
    b.get
  }

  def getBytesFromRawBuffer( buffer : RawBuffer) = {
    buffer.asBytes() match{
      case Some(bytes) => bytes
      case None => {
        val f = buffer.asFile
        val in = new FileInputStream(f)
        try{
          val d = new Array[Byte](buffer.size.toInt)
          in.read(d)
          d
        }finally{
          in.close()
        }
      }
    }
  }


}
