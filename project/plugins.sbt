resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url("heroku-sbt-plugin-releases",
  url("https://dl.bintray.com/heroku/sbt-plugins/"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.heroku" % "sbt-heroku" % "0.3.0")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.7")

// web plugins

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

addSbtPlugin("net.ground5hark.sbt" % "sbt-concat" % "0.1.8")

// This plugin automatically refreshes Chrome when you make changes to your app
addSbtPlugin("com.jamesward" %% "play-auto-refresh" % "0.0.11")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.3")
