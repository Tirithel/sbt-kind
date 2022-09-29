package sbtkind

import sbt._
import sbt.Keys.target

import sbtkind.KindKeys._

object KindSettings {

  lazy val baseKindSettings = Seq(
    kind := {
      val log         = Keys.streams.value.log
      val clusterName = (kind / KindKeys.clusterName).value
      val imageNames  = (kind / KindKeys.dockerImageNames).value

      LoadImages(clusterName, imageNames, log)
    },
    kind / clusterName := {
      sys.error(
        """A cluster name is not defined. Please define one with `kind / clusterName
          |
          |Example:
          |kind / clusterName := {
          |  import scala.sys.process._
          |  "whoami" !!
          |}
          |""".stripMargin)
    },
    kind / dockerImageNames := {
      val log = Keys.streams.value.log

      val organization = Option(Keys.organization.value).filter(_.nonEmpty)
      val name         = Keys.normalizedName.value

      organization match {
        case Some(org) =>
          log.warn(
            s"""Using default `kind / imageNames`. This value defaults to Seq($org/$name)
               |which is the same as the default for `docker / imageNames`. 
               |
               |These values come from the organization and name key fields respectively. 
               |""".stripMargin)

          Seq(s"$org/$name")
        case None =>
          log.warn(
            s"""Using default `kind / imageNames`. This value defaults to Seq($name)
               |which is the same as the default for `docker / imageNames`. 
               |
               |This values comes from the name key field. 
               |""".stripMargin)

          Seq(s"$name")
      }
    },
  )

}
