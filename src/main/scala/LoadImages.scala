package sbtkind

import java.util.UUID

import sbt.*

import scala.concurrent.{ ExecutionContext, Future }
import scala.sys.process.*

import cats.data.EitherT
import cats.implicits.*
import sbtkind.util.FuncUtil.handleResponse
import ExecutionContext.Implicits.global

object LoadImages {
  var lines = Seq.empty[String]

  def apply(clusterName: String, images: Seq[String])(
    implicit log: Logger): Either[Error, Seq[Future[Unit]]] = {
    val v = "kind get clusters" !! throwAwayProcessLogger

    if (v.contains(clusterName)) {
      val f =
        for {
          result <- images.map(img => load(clusterName, img))
        } yield handleResponse(
          "LoadImages",
          result.value,
          onError = (error: Error) => {
            log.error(error.getMessage)
          },
          onException = e => log.error(e.getMessage),
          onSuccess = (load: KindLoad) => {
            log.success(s"Successfully loaded ${load.id}")
          },
        )

      Right(f)
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
    implicit log: Logger): EitherT[Future, Error, KindLoad] = {
    val kindLoad = KindLoad(UUID.randomUUID)

    log.debug(s"kind load docker-image $image --name $clusterName")
    log.info(s"Loading $image with id ${kindLoad.id}")
    val result = s"kind load docker-image $image --name $clusterName" ! lineInfoProcessLogger

    result match {
      case 0 => EitherT.rightT(KindLoad(UUID.randomUUID()))
      case _ => EitherT.leftT(KindLoadError(s"Error loading image $image with id ${kindLoad.id}"))
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
