name := "mystique-api"

fork in run := true

version := "1.0"

scalaVersion := "2.11.5"

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Twitter repo" at "http://maven.twttr.com/",
  "Sedis" at "http://pk11-scratch.googlecode.com/svn/trunk",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.twitter" % "finagle-redis_2.11" % "6.26.0",
  "com.twitter" % "twitter-server_2.11" % "1.11.0",
  "com.twitter" % "finagle-core_2.11" % "6.26.0",
  "com.twitter" %% "finagle-http" % "6.26.0",
  "joda-time" % "joda-time" % "2.8.1",
  "com.typesafe" % "config" % "1.3.0"
)

// test
libraryDependencies ++= Seq(
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)

ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 65

addCommandAlias("test", "testQuick")

addCommandAlias("devrun", "~re-start")

addCommandAlias("cov", "; clean; coverage; test")

Revolver.settings
