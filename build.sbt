organization in ThisBuild := "me.kerfume"
version in ThisBuild := "0.1.0"
scalaVersion in ThisBuild := Base.scala213
scalafmtOnCompile in ThisBuild := true

lazy val productionBuild =
  taskKey[Unit]("format check and compile and test")

lazy val supportVersions = Seq(
  Base.scala212,
  Base.scala213
)

lazy val core = Core.core
lazy val nightly =
  Nightly.nightly.dependsOn(core % "test->test;compile->compile")

lazy val sourceProjects = Seq(core, nightly)

lazy val root = (project in file("."))
  .aggregate(core, nightly)

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
          scalaVersion in ThisBuild := sv,
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
