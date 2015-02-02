import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import com.typesafe.sbt.jshint.Import.JshintKeys
import com.typesafe.sbt.less.Import.LessKeys
import com.typesafe.sbt.web.Import.WebKeys
import com.typesafe.sbt.rjs.Import.RjsKeys
import com.typesafe.sbt.web.js.JS
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
  "org.webjars" % "requirejs" % "2.1.15",
  "org.webjars" % "underscorejs" % "1.7.0",
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "bootstrap" % "3.3.1" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.2.27" exclude("org.webjars", "jquery"),
  "org.webjars" % "angular-sanitize" % "1.2.26" exclude("org.webjars", "jquery"),
  "org.webjars" % "angular-ui-bootstrap" % "0.12.0" exclude("org.webjars", "angularjs"),
  "org.webjars" % "angular-ui-router" % "0.2.13" exclude("org.webjars", "angularjs"),
  "org.webjars" % "angular-route-segment" % "1.3.3" exclude("org.webjars", "angularjs"),
  "org.webjars" % "angular-blocks" % "0.1.8-1" exclude("org.webjars", "angularjs"),
  "org.webjars" % "font-awesome" % "4.2.0",
  "org.webjars" % "json3" % "3.3.2-1",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "metisMenu" % "1.1.2",
  "org.webjars" % "morrisjs" % "0.5.1",
  "org.webjars" % "raphaeljs" % "2.1.2-1",
  "org.scalatestplus" %% "play" % "1.2.0" % "test"
)

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

//
// sbt-web configuration
// https://github.com/sbt/sbt-web
//

// Configure the steps of the asset pipeline (used in stage and dist tasks)
// rjs = RequireJS, uglifies, shrinks to one file, replaces WebJars with CDN
// digest = Adds hash to filename
// gzip = Zips all assets, Asset controller serves them automatically when client accepts them
// Uncomment this if you want to run all the asset pipeline stages for production
//pipelineStages := Seq(rjs, concat, cssCompress, digest, gzip)

// RequireJS with sbt-rjs (https://github.com/sbt/sbt-rjs#sbt-rjs)
// ~~~
// The r.js optimizer won't find jsRoutes so we must tell it to ignore it
// Override RequireJS path mappings: module_id -> (build_path -> production_path)
RjsKeys.paths += ("jsRoutes" -> ("/jsroutes" -> "empty:"))

val checkCdn = taskKey[Unit]("Check the CDN")

checkCdn := println(RjsKeys.paths.value)

RjsKeys.generateSourceMaps := false

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

excludeFilter in cssCompress := GlobFilter("lib/*.css")

// Exlude third-party plugins in app/assets/javascripts/plugin from jshint targets
excludeFilter in (Assets, JshintKeys.jshint) := new FileFilter{
  def accept(f: File) = ".*/plugins/.*".r.pattern.matcher(f.getAbsolutePath).matches
}

// This must be turned off when deployed to heroku
//JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

//RjsKeys.mainModule := "main"

// Asset hashing with sbt-digest (https://github.com/sbt/sbt-digest)
// ~~~
// md5 | sha1
//DigestKeys.algorithms := "md5"
//includeFilter in digest := "..."
//excludeFilter in digest := "..."

// HTTP compression with sbt-gzip (https://github.com/sbt/sbt-gzip)
// ~~~
// includeFilter in GzipKeys.compress := "*.html" || "*.css" || "*.js"
// excludeFilter in GzipKeys.compress := "..."

// JavaScript linting with sbt-jshint (https://github.com/sbt/sbt-jshint)
// ~~~
// JshintKeys.config := ".jshintrc"

// Disable generation of scaladoc in dist task
publishArtifact in (Compile, packageDoc) := false

publishArtifact in packageDoc := false

sources in (Compile,doc) := Seq.empty


// All work and no play...
emojiLogs


// heroku
herokuAppName in Compile := "warm-beyond-9991"
