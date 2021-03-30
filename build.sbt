scalaVersion := "2.13.4"

import Settings._

val scalas = Seq("2.12.13", "2.13.4")

val CE3_Version = "3.0.1"
val CE2_Version = "2.4.1"

lazy val core = projectMatrix
  .in(file("core"))
  .defaultAxes(
    CatsEffect3Axis,
    VirtualAxis.jvm,
    VirtualAxis.scalaABIVersion("2.13.4")
  )
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
        "org.typelevel"    %% "cats-effect" % CE3_Version
      else "org.typelevel" %% "cats-effect" % CE2_Version
    },
    libraryDependencies += "org.scalameta" %% "mdoc" % "2.2.18" % "provided"
  )
  .settings(moduleName := {
    if (virtualAxes.value.contains(CatsEffect3Axis)) "mdoc-effect-ce3"
    else "mdoc-effect-ce2"
  })

lazy val core_CE2 = core.finder(CatsEffect2Axis)("2.13.4")
lazy val core_CE3 = core.finder(CatsEffect3Axis)("2.13.4")

lazy val docs = project
  .in(file("docs"))
  .dependsOn(
    core_CE2,
    core_CE3
  ) // there's actually no compilation going on, we just want it to be triggered
  .settings(scalaVersion := "2.13.4")
  .enablePlugins(SubatomicPlugin)
  .settings(
    skip in publish := true,
    subatomicDependencies ++= Seq(
      Subatomic.path(
        (core_CE2 / Compile / target).value / "classes",
        "cats-effect-2"
      ),
      Subatomic.paths(
        (core_CE2 / Compile / resourceDirectories).value,
        "cats-effect-2"
      ),
      Subatomic.dependency(
        "org.typelevel" %% "cats-effect" % CE2_Version,
        "cats-effect-2"
      ),
      Subatomic.path(
        (core_CE3 / Compile / target).value / "classes",
        "cats-effect-3"
      ),
      Subatomic.paths(
        (core_CE3 / Compile / resourceDirectories).value,
        "cats-effect-3"
      ),
      Subatomic.dependency(
        "org.typelevel" %% "cats-effect" % CE3_Version,
        "cats-effect-3"
      )
    ),
    watchSources += WatchSource(
      (baseDirectory in ThisBuild).value / "docs" / "pages"
    ),
    unmanagedSourceDirectories in Compile +=
      (baseDirectory in ThisBuild).value / "docs"
  )

inThisBuild(
  Seq(
    resolvers += Resolver.sonatypeRepo("snapshots"),
    organization := "com.indoorvivants",
    organizationName := "Anton Sviridov",
    homepage := Some(url("https://github.com/indoorvivants/mdoc-effect")),
    startYear := Some(2021),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "keynmol",
        "Anton Sviridov",
        "keynmol@gmail.com",
        url("https://blog.indoorvivants.com")
      )
    )
  )
)
