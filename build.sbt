name := """Deep Dive"""

version := "0.0.3"

scalaVersion := "2.11.5"

organization in ThisBuild := "atWare, inc"

enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  filters,
  ws,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "info.folone" %% "poi-scala" % "0.14",
  "org.specs2" % "specs2_2.11" % "2.3.12-scalaz-7.1.0-M7",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test"
)

// Scala Compiler Options
scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation", // warning and locaation for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-language:postfixOps"
)

// heroku
enablePlugins(HerokuPlugin)

herokuAppName in Compile := "warm-beyond-9991"

herokuJdkVersion in Compile := "1.8"

net.virtualvoid.sbt.graph.Plugin.graphSettings
