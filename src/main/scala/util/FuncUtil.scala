package sbtkind.util

import sbt.*

import scala.concurrent.{ ExecutionContext, Future }

import sbtkind.KindLoadError

object FuncUtil {

  def handleResponse[E, T, R](
    func: String,
    response: Future[Either[E, T]],
    onError: E => R,
    onException: Exception => R,
    onSuccess: T => R)(
    implicit ec: ExecutionContext,
    log: Logger): Future[R] = response.map(
    _.fold(
      error => handleError(func, error, onError),
      success => {
        onSuccess(success)
      },
    )
  ).recover {
    case e: Exception => onException(e)
  }

  def handleError[E, R](func: String, error: E, onError: E => R)(
    implicit log: Logger): R = {
    val t = error match {
      case _: KindLoadError => "load_error"
      case _                => "unknown"
    }
    log.error(s"[$t] occurred in $func.")
    onError(error)
  }

}
