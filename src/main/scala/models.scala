package sbtkind

import java.util.UUID

final case class KindLoad(id: UUID) extends AnyVal {
  override def toString = id.toString
}

final case class KindLoadError(msg: String = "Error loading image.")                           extends Error(msg)
final case class KindClusterDoesNotExistError(msg: String = "Cluster provided doesn't exist.") extends Error(msg)
