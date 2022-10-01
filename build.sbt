ThisBuild / scalaVersion := "2.12.17"

import sbt.ScriptedPlugin.autoImport.scriptedLaunchOpts

import sbtrelease._
import ReleaseStateTransformations._

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
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials"),
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      releaseStepTask(scalafmtCheckAll),
      runTest,
      releaseStepInputTask(scripted),
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommand("publishSigned"),
      releaseStepCommand("sonatypeBundleRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges,
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
