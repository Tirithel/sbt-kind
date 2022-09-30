package sbtkind

import sbt.*

import scala.sys.process.*

object LoadImages {
  var lines = Seq.empty[String]

  def apply(clusterName: String, images: Seq[String])(
    implicit log: Logger): Either[Error, Seq[KindLoad]] = {
    val v = "kind get clusters" !! throwAwayProcessLogger

    if (v.contains(clusterName)) {
      images.map(image => load(clusterName, image)).foldRight(Right(Nil): Either[Error, List[KindLoad]]) {
        (e, acc) =>
          if (e.isLeft) log.error(e.left.get.getMessage)
          for {
            xs <- acc.right
            x  <- e.right
          } yield x :: xs
      }
    } else {
      Left(KindClusterDoesNotExistError(s"Cluster $clusterName does not exist."))
    }
  }

  /**
    * Loads a single image into a given cluster.
    * @param clusterName
    * @param image
    * @param log
    * @return
    */
  private[this] def load(clusterName: String, image: String)(
    implicit log: Logger): Either[Error, KindLoad] = {
    val kindLoad = KindLoad(image)

    log.debug(s"kind load docker-image $image --name $clusterName")
    log.info(s"Loading ${kindLoad.image}...")
    val result = s"kind load docker-image $image --name $clusterName" ! lineInfoProcessLogger

    result match {
      case 0 => Right(kindLoad)
      case _ => Left(KindLoadError(s"Error loading image ${kindLoad.image}"))
    }
  }

  private val throwAwayProcessLogger = ProcessLogger(_ => ())

  private[this] def lineInfoProcessLogger(
    implicit log: Logger): ProcessLogger = ProcessLogger(
    { line =>
      log.info(line)
      lines :+= line
    },
    { line =>
      log.warn(line)
      lines :+= line
    },
  )

}
