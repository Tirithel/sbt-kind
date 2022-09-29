package sbtkind

import java.util.UUID

final case class KindLoad(id: UUID) extends AnyVal {
  override def toString = id.toString
}

final case class KindLoadError(msg: String = "Error loading image.") extends Error(msg)
