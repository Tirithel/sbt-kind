package sbtkind

import sbt.*

import scala.concurrent.Future

object KindKeys {
  val kind             = taskKey[Seq[Future[Unit]]]("Load a kind cluster.")
  val clusterName      = taskKey[String]("Cluster name to load Docker image to.")
  val dockerImageNames = taskKey[Seq[String]]("Sequence of Docker images, including tag, to load into the cluster. ")
}
