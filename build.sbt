import Dependencies._

lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.1"

organization in ThisBuild := "me.kerfume"
version in ThisBuild := "0.1.0-SNAPSHOT"
scalaVersion in ThisBuild := scala213

lazy val publishAll = taskKey[Unit]("compile and publish command with all supprt scala versions")
lazy val supprtVersions = Seq(scala212, scala213)

publishAll := {
  val baseState = state.value

  supprtVersions.foreach { sv =>
    val ns = Project.extract(baseState).appendWithSession(
    Seq(
      scalaVersion := sv
    ), baseState)
    Project.extract(ns).runTask(clean in Test, ns)
    Project.extract(ns).runTask(compile in Test, ns)
    Project.extract(ns).runTask(test in Test, ns)
    Project.extract(ns).runTask(publish in Compile, ns)
  }
}

lazy val root = (project in file("."))
  .settings(
    crossScalaVersions := supprtVersions,
    name := "kerfume-scala-util",
    publishMavenStyle := true,
    publishTo := Some(Resolver.file("core", file("repo"))),
    libraryDependencies += scalaTest % Test
  )

// Uncomment the following for publishing to Sonatype.
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for more detail.

// ThisBuild / description := "Some descripiton about your project."
// ThisBuild / licenses    := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
// ThisBuild / homepage    := Some(url("https://github.com/example/project"))
// ThisBuild / scmInfo := Some(
//   ScmInfo(
//     url("https://github.com/your-account/your-project"),
//     "scm:git@github.com:your-account/your-project.git"
//   )
// )
// ThisBuild / developers := List(
//   Developer(
//     id    = "Your identifier",
//     name  = "Your Name",
//     email = "your@email",
//     url   = url("http://your.url")
//   )
// )
// ThisBuild / pomIncludeRepository := { _ => false }
// ThisBuild / publishTo := {
//   val nexus = "https://oss.sonatype.org/"
//   if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
//   else Some("releases" at nexus + "service/local/staging/deploy/maven2")
// }

