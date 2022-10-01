import sbt._

import sbtrelease._
import xerial.sbt.Sonatype._
import ReleaseStateTransformations._

organization := "org.cmoran"

sonatypeProjectHosting := Some(GitHubHosting("tirithel", "sbt-kind", "colin.mtech@gmail.com"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/Tirithel/sbt-kind"),
    "scm:git@github.com:Tirithel/sbt-kind.git",
  )
)

description := "sbt plugin that loads built Docker images for your project into a kind cluster."
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
homepage := Some(url("https://github.com/Tirithel/sbt-kind"))

// Remove all additional repository other than Maven Central from POM
pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

sonatypeCredentialHost := "s01.oss.sonatype.org"
publishMavenStyle      := true

credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")

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
)
