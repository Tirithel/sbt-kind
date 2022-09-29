package sbtkind.util

import sbt.Keys

object implicits {
  implicit val log = Keys.streams.value.log
}
