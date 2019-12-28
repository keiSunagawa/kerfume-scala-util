organization in ThisBuild := "me.kerfume"
version in ThisBuild := "0.1.0-SNAPSHOT"
scalaVersion in ThisBuild := Base.scala213
scalafmtOnCompile in ThisBuild := true

lazy val productionBuild =
  taskKey[Unit]("format check and compile and test")

lazy val publishAll =
  taskKey[Unit]("compile and publish command with all supprt scala versions")

lazy val supportVersions = Seq(
  Base.scala212,
  Base.scala213
)

lazy val core = Core.define

lazy val sourceProjects = Seq(core)

lazy val root = (project in file("."))
  .aggregate(core)

productionBuild := {

  val baseState = state.value

  for {
    pj <- sourceProjects
    sv <- supportVersions
  } yield {
    val ns = Project
      .extract(baseState)
      .appendWithSession(
        Seq(
          scalaVersion := sv,
          scalacOptions in ThisBuild ++= Seq(
            "-Xfatal-warnings"
          )
        ),
        baseState
      )
    Project.extract(ns).runTask(clean in (pj, Test), ns)
    Project.extract(ns).runTask(scalafmtCheckAll in Test, ns)
    Project.extract(ns).runTask(scalafmtSbtCheck in Test, ns)
    Project.extract(ns).runTask(compile in (pj, Test), ns)
    Project.extract(ns).runTask(test in (pj, Test), ns)
  }
}
publishAll := {
  val baseState = state.value

  productionBuild.value

  for {
    pj <- sourceProjects
    sv <- supportVersions
  } yield {
    val ns = Project
      .extract(baseState)
      .appendWithSession(
        Seq(
          scalaVersion in ThisBuild := sv
        ),
        baseState
      )

    Project.extract(ns).runTask(publish in (pj, Compile), ns)
  }
}

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
