package models

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 13/06/24
 * Time: 3:18
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

object UploadedImage extends UploadedImage with LongKeyedMetaMapper[UploadedImage]{


}
class UploadedImage extends LongKeyedMapper[UploadedImage] with IdPK{

  def getSingleton = UploadedImage

  object fileKey extends MappedString(this,128)
  object uploadedUser extends MappedLongForeignKey(this,User)
  object uploaded extends MappedDateTime(this)
  object hasGpsInfo extends MappedBoolean(this)
  object longitude extends MappedDouble(this)
  object latitude extends MappedDouble(this)
}
