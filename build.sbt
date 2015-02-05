import play.PlayScala

name := """Deep Dive"""

version := "0.0.2"

organization in ThisBuild := "atWare, inc"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  filters,
  ws,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "react" % "0.12.2",
  "org.webjars" % "react-router" % "0.11.6",
  "org.webjars" % "showdown" % "0.3.1",
  "org.webjars" % "jquery" % "1.10.2-1"
)

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.7",
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

// Asset hashing with sbt-digest (https://github.com/sbt/sbt-digest)
// ~~~
// md5 | sha1
//DigestKeys.algorithms := "md5"
//includeFilter in digest := "..."
//excludeFilter in digest := "..."

// HTTP compression with sbt-gzip (https://github.com/sbt/sbt-gzip)
// ~~~
//includeFilter in GzipKeys.compress := "*.html" || "*.css" || "*.js"
//excludeFilter in GzipKeys.compress := "..."


// Disable generation of scaladoc in dist task
publishArtifact in (Compile, packageDoc) := false

publishArtifact in packageDoc := false

sources in (Compile,doc) := Seq.empty


// All work and no play...
emojiLogs


// heroku
herokuAppName in Compile := "warm-beyond-9991"
