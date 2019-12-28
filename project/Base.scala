import sbt._
import Keys._
import Dependencies._

object Base {
  lazy val scala212 = "2.12.10"
  lazy val scala213 = "2.13.1"
  lazy val supprtVersions = Seq(scala212, scala213)

  val commonLibs = cats ++ testDep

  private val version212OnlyOptions = Seq(
    "-Ypartial-unification",
    "-language:higherKinds",
  )
  def commonScalaOptions(currentVersion: String) = Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    //"-Xfatal-warnings",
  ) ++ (if (currentVersion == Base.scala212) Base.version212OnlyOptions else Nil)

  val settings = Seq(
    crossScalaVersions := supprtVersions,
    publishMavenStyle := true,
  )
}
