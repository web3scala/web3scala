name := "core"

organization := "org.web3scala"

version := "0.1.0"

scalaVersion := "2.12.3"

crossPaths := false

organizationHomepage := Some(url("http://web3scala.org"))

startYear := Some(2017)

description := "Scala library for integration with Ethereum clients"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.13.1",
  "net.databinder.dispatch" %% "dispatch-json4s-jackson" % "0.13.1",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.9.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.mockito" % "mockito-core" % "2.9.0" % "test"
)

dependencyOverrides ++= Set(
  "io.netty" % "netty-handler" % "4.0.48.Final"
)

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

coverageEnabled in Test := true