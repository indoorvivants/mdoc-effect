import Settings._

val Scala213 = "2.13.10"
val Scala212 = "2.12.17"
val Scala3   = "3.2.2"

val scalas = Seq(Scala212, Scala213, Scala3)


val CE3_Version = "3.4.6"

val CE2_Version = "2.5.5"

lazy val core = projectMatrix
  .in(file("core"))
  .defaultAxes(
    CatsEffect3Axis,
    VirtualAxis.jvm,
    VirtualAxis.scalaABIVersion(Scala213)
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
    libraryDependencies += "org.scalameta" %% "mdoc" % "2.3.6" % "provided"
  )
  .settings(moduleName := {
    if (virtualAxes.value.contains(CatsEffect3Axis)) "mdoc-effect-ce3"
    else "mdoc-effect-ce2"
  })

lazy val core_CE2 = core.finder(CatsEffect2Axis)(Scala213)
lazy val core_CE3 = core.finder(CatsEffect3Axis)(Scala213)

lazy val docs = project
  .in(file("docs"))
  .settings(scalaVersion := Scala213)
  .enablePlugins(SubatomicPlugin)
  .settings(
    publish / skip := true,
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
      (ThisBuild / baseDirectory).value / "docs" / "pages"
    ),
    Compile / unmanagedSourceDirectories +=
      (ThisBuild / baseDirectory).value / "docs"
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
