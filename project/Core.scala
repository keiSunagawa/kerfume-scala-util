import sbt._
import Keys._
import Dependencies._

object Core {
  lazy val core = (project in file("core"))
    .settings(Base.settings)
    .settings(
      name := "kerfume-scala-util-core",
      scalacOptions ++= Base.commonScalaOptions(scalaVersion.value),
      libraryDependencies ++= Base.commonLibs
    )
}
