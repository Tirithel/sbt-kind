package sbtkind

final case class KindLoad(image: String)                                                       extends AnyVal
final case class KindLoadError(msg: String = "Error loading image.")                           extends Error(msg)
final case class KindClusterDoesNotExistError(msg: String = "Cluster provided doesn't exist.") extends Error(msg)
