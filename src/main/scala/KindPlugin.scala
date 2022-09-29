package sbtkind

import sbt._

object KindPlugin extends AutoPlugin {

  object autoImport {
    val KindKeys = sbtkind.KindKeys

    val kind             = KindKeys.kind
    val clusterName      = KindKeys.clusterName
    val dockerImageNames = KindKeys.dockerImageNames
  }

  override def projectSettings = KindSettings.baseKindSettings
}
