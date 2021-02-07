scalaVersion := "2.13.4"

lazy val core = project
  .in(file("core"))
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.0.0-M5"
  )

lazy val docs = project
  .in(file("ce-docs"))
  .dependsOn(core)
  .enablePlugins(MdocPlugin)
  .settings(
    watchSources += WatchSource(
      (baseDirectory in ThisBuild).value / "docs"
    ),
    Compile / unmanagedSourceDirectories += (ThisBuild / baseDirectory).value / "docs" / "scala-sources"
  )
