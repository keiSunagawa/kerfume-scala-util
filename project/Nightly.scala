import sbt._
import Keys._
import Dependencies._

object Nightly {
  lazy val nightly = (project in file("nightly"))
    .settings(Base.settings)
    .settings(
      name := "kerfume-scala-util-nightly",
      scalacOptions ++= Base.commonScalaOptions(scalaVersion.value) ++ Seq(
        "-Ywarn-unused:-patvars" // for BiPath quasiquoat unapply matching
      ),
      libraryDependencies ++= Seq(
        scalaOrganization.value % "scala-reflect" % scalaVersion.value
      ) ++ Base.commonLibs
    )
}
