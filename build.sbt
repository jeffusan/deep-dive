import play.PlayScala

name := """Deep Dive"""

version := "0.0.3"

organization in ThisBuild := "atWare, inc"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  filters,
  ws,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.scalatestplus" %% "play" % "1.2.0" % "test"
)

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
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
herokuAppName in Compile := "warm-beyond-9991"
