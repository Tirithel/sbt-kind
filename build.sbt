ThisBuild / scalaVersion := "2.12.17"

import sbt.ScriptedPlugin.autoImport.scriptedLaunchOpts

onLoadMessage := s"Welcome to sbt-ci-release ${version.value}"

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-kind",
    scriptedLaunchOpts ++= Seq(
      "-Xmx1024M",
      "-Dplugin.version=" + version.value,
    ),
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials"),
    releaseProcess := Seq[ReleaseStep](
      releaseStepCommand("sonatypeOpen \"org.cmoran\" \"Some staging name\""),
      releaseStepCommand("publishSigned"),
      releaseStepCommand("sonatypeRelease"),
    ),
  )
