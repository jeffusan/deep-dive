resolvers += Resolver.url("heroku-sbt-plugin-releases",
  url("https://dl.bintray.com/heroku/sbt-plugins/"))(Resolver.ivyStylePatterns)

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.7")

addSbtPlugin("com.heroku" % "sbt-heroku" % "0.3.0")

scalacOptions ++= (
"-deprecation" ::
"-unchecked" ::
"-language:existentials" ::
"-language:higherKinds" ::
"-language:implicitConversions" ::
Nil
)
