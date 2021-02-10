resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("com.eed3si9n"              % "sbt-projectmatrix"         % "0.7.0")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"              % "0.1.13")
addSbtPlugin("com.github.cb372"          % "sbt-explicit-dependencies" % "0.2.13")
addSbtPlugin("org.scalameta"             % "sbt-scalafmt"              % "2.4.2")
addSbtPlugin("com.geirsson"              % "sbt-ci-release"            % "1.5.5")
addSbtPlugin("ch.epfl.scala"             % "sbt-scalafix"              % "0.9.25")
addSbtPlugin("de.heikoseeberger"         % "sbt-header"                % "5.6.0")

// 0.0.5+15-004f1618+20210208-1908-SNAPSHOT
addSbtPlugin(
  "com.indoorvivants" % "subatomic-plugin" % "0.0.5+18-60696667-SNAPSHOT"
)
