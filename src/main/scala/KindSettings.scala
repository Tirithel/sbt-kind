package sbtkind

import sbt.*

import sbtkind.KindKeys.*

object KindSettings {

  lazy val baseKindSettings = Seq(
    kind := {
      implicit val log = Keys.streams.value.log

      val clusterName = (kind / KindKeys.clusterName).value
      val imageNames  = (kind / KindKeys.dockerImageNames).value

      LoadImages(clusterName, imageNames) match {
        case Left(e)  => log.error(e.getMessage)
        case Right(r) => r.foreach(load => log.success(s"Successfully loaded ${load.image} to $clusterName."))
      }
    },

    /**
      * The `clusterName` will default to an error since we cannot assume which
      * if any cluster the user wants to load images to.
      *
      * Defaults to: None, will exit with an error.
      */
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

    /**
      * The key `dockerImageNames` will default the same way the sbt-docker
      * plugin works. This is to try to achieve out of the box compatibility.
      *
      * Defaults to: `project-organization`/`project-name`
      */
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
