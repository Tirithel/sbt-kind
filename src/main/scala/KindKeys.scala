package sbtkind

import sbt.*

object KindKeys {
  val kind             = taskKey[Unit]("Load a kind cluster.")
  val clusterName      = taskKey[String]("Cluster name to load Docker image to.")
  val dockerImageNames = taskKey[Seq[String]]("Sequence of Docker images as strings, including optional tag, to load into the cluster. ")
}
