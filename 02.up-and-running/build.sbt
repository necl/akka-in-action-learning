
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "1.0"
ThisBuild / organization := "com.goticks"
name := "goticks"

unmanagedClasspath in Runtime += baseDirectory.value

libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.3.4",
        "com.typesafe.akka" %% "akka-actor" % "2.5.22",
        "com.typesafe.akka" %% "akka-stream" % "2.5.22",
        "com.typesafe.akka" %% "akka-http" % "10.1.8",
    )
