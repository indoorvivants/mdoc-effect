scalaVersion := "2.13.4"

import Settings._

val scalas = Seq("2.12.13", "2.13.4")

lazy val core = projectMatrix
  .in(file("core"))
  .defaultAxes(CatsEffect3Axis, VirtualAxis.jvm, VirtualAxis.scalaABIVersion("2.13.4"))
  .customRow(
    scalaVersions = scalas,
    axisValues = Seq(CatsEffect2Axis, VirtualAxis.jvm),
    identity[Project] _
  )
  .customRow(
    scalaVersions = scalas,
    axisValues = Seq(CatsEffect3Axis, VirtualAxis.jvm),
    identity[Project] _
  )
  .settings(
    libraryDependencies += {
      if (virtualAxes.value.contains(CatsEffect3Axis))
        "org.typelevel" %% "cats-effect" % "3.0.0-M5"
      else "org.typelevel" %% "cats-effect" % "2.3.1"
    },
    libraryDependencies += "org.scalameta" %% "mdoc" % "2.2.17"
  )

lazy val coreJVM = core.finder(CatsEffect3Axis)("2.13.4")

lazy val docs = project
  .in(file("docs"))
  .settings(scalaVersion := "2.13.4")
  .dependsOn(coreJVM)
  .enablePlugins(SubatomicPlugin)
  .enablePlugins(MdocPlugin)
  .settings(
    skip in publish := true,
    subatomicMdocPlugins += subatomicMdocPlugin((coreJVM / Compile / target).value / "classes"),
    subatomicMdocPlugins += subatomicMdocPlugin("org.typelevel" %% "cats-effect" % "3.0.0-M5"),
    watchSources += WatchSource(
      (baseDirectory in ThisBuild).value / "docs" / "pages"
    ),
    unmanagedSourceDirectories in Compile +=
      (baseDirectory in ThisBuild).value / "docs"
  )
