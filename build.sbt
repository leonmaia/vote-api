name := "mystique-api"

fork in run := true

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.twitter" % "finagle-redis_2.11" % "6.26.0",
  "com.twitter" % "twitter-server_2.11" % "1.11.0",
  "com.twitter" % "finagle-core_2.11" % "6.26.0",
  "com.twitter" %% "finagle-http" % "6.26.0",
  "com.typesafe" % "config" % "1.3.0"
)

// test
libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)

ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 65

addCommandAlias("test", "testQuick")

addCommandAlias("devrun", "~re-start")

addCommandAlias("cov", "; clean; coverage; test")
