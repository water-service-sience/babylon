package controllers.manager

import play.api.mvc.{AnyContent, Request}
import controllers.APIException
import play.api.Configuration
import util.{EncryptUtil, FileUtil}
import models.UploadedImage

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/26
 * Time: 0:22
 * To change this template use File | Settings | File Templates.
 */
object PhotoManager {

  lazy val photoDir = {
    "/data"
  }

  def saveUploadedFile(userId : Long)(implicit req : Request[AnyContent]) = {
    req.body.asRaw match{
      case Some(buffer) => {
        val b = buffer.asBytes().get
        val filename = "u" + userId + "/" + EncryptUtil.sha1Digest(b)
        FileUtil.saveTo(photoDir + filename,b)

        UploadedImage.create(userId,filename)
      }
      case None => {
        throw new APIException("Fail to get photo")
      }
    }

  }


}
