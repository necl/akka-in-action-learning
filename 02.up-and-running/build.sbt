
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "1.0"
ThisBuild / organization := "com.goticks"
name := "goticks"

libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.3.4",
        "com.typesafe.akka" %% "akka-actor" % "2.5.22",
    )
