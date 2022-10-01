ThisBuild / scalaVersion := "2.12.17"

import sbt.ScriptedPlugin.autoImport.scriptedLaunchOpts

releasePublishArtifactsAction := PgpKeys.publishSigned.value

onLoadMessage := s"Welcome to sbt-ci-release ${version.value}"

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-kind",
    scriptedLaunchOpts ++= Seq(
      "-Xmx1024M",
      "-Dplugin.version=" + version.value,
    ),
    scalacOptions := Seq(
      "-encoding",
      "UTF-8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xfuture",
    ),
  )
