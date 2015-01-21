name := "math-core"

organization := "org.fathens"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.4"

scalacOptions ++= Seq(
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-feature"
)

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-scalacheck" % "2.4.15" % "test" withSources,
  "org.scalaz" %% "scalaz-core" % "7.1.0" withSources
)
