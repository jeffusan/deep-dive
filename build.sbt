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
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "org.specs2" % "specs2_2.11" % "2.3.12-scalaz-7.1.0-M7",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test",
  "org.apache.poi" % "poi" % "3.11",
  "org.apache.poi" % "poi-ooxml" % "3.11",
  // WebJars (i.e. client-side) dependencies
  "org.webjars" % "requirejs" % "2.1.14-1",
  "org.webjars" % "underscorejs" % "1.6.0-3",
  "org.webjars" % "jquery" % "1.11.1",
  "org.webjars" % "bootstrap" % "3.3.4" exclude("org.webjars", "jquery"),
  "org.webjars" % "react" % "0.13.0",
  "org.webjars" % "react-router" % "0.13.2",
  "org.webjars" % "react-bootstrap" % "0.13.2",
  "org.webjars" % "font-awesome" % "4.3.0-1",
  "org.webjars" % "x-editable-bootstrap3" % "1.5.1",
  "org.webjars" % "jquery-file-upload" % "9.8.1",
  "org.webjars" % "jsx-requirejs-plugin" % "0.5.2",
  "org.webjars" % "bootstrap-datepicker" % "1.4.0"
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

enablePlugins(SbtWeb)

// heroku
enablePlugins(HerokuPlugin)

herokuAppName in Compile := "warm-beyond-9991"

herokuJdkVersion in Compile := "1.8"

net.virtualvoid.sbt.graph.Plugin.graphSettings

pipelineStages := Seq(digest, gzip)

// RequireJS with sbt-rjs (https://github.com/sbt/sbt-rjs#sbt-rjs)
// ~~~
RjsKeys.paths += ("jsRoutes" -> ("/jsroutes" -> "empty:"))

emojiLogs
