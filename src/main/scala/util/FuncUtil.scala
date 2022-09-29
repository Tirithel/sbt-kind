package sbtkind.util

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

import sbtkind.KindLoadError

object FuncUtil {

  def handleResponse[E, T, R](
    func: String,
    response: Future[Either[E, T]],
    onError: E => R,
    onException: Exception => R,
    onSuccess: T => R)(
    implicit ec: ExecutionContext): Future[R] = response.map(
    _.fold(
      error => handleError(func, error, onError),
      success => {
        onSuccess(success)
      },
    )
  ).recover {
    case e: Exception => onException(e)
  }

  def handleError[E, R](func: String, error: E, onError: E => R): R = {
    val t = error match {
      case _: KindLoadError => "load_error"
      case _                => "unknown"
    }

    onError(error)
  }

}
