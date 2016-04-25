/*
import sbt._
import Keys._

object ExampleBuild extends Build {

  val dependencies = Seq(
    "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )

  lazy val exampleProject = Project("SbtExample", file(".")) settings(
    version       := "0.2",
    scalaVersion  := "2.10.0",
    scalacOptions := Seq("-deprecation"),
    libraryDependencies ++= dependencies
    )

}
*/
/*
import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.scalamacros",
    version := "1.0.0",
    scalaVersion := "2.11.8",
    crossScalaVersions := Seq("2.10.2", "2.10.3", "2.10.4", "2.10.5", "2.10.6", "2.11.0", "2.11.1", "2.11.2", "2.11.3", "2.11.4", "2.11.5", "2.11.6", "2.11.7", "2.11.8"),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.sonatypeRepo("releases"),
    scalacOptions ++= Seq()
  )
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings ++ Seq(
      run <<= run in Compile in core)
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies := {
        CrossVersion.partialVersion(scalaVersion.value) match {
          // if Scala 2.11+ is used, quasiquotes are available in the standard distribution
          case Some((2, scalaMajor)) if scalaMajor >= 11 =>
            libraryDependencies.value
          // in Scala 2.10, quasiquotes are provided by macro paradise
          case Some((2, 10)) =>
            libraryDependencies.value ++ Seq(
              compilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full),
              "org.scalamacros" %% "quasiquotes" % "2.1.0-M5" cross CrossVersion.binary)
        }
      }
    )
  )

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = buildSettings
  ) dependsOn(macros)
}
*/

/**
Sample Build.scala:

import sbt._
import Keys._

object ExampleBuild extends Build {

  val dependencies = Seq(
    "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )

  lazy val exampleProject = Project("SbtExample", file(".")) settings(
    version       := "0.2",
    scalaVersion  := "2.10.0",
    scalacOptions := Seq("-deprecation"),
    libraryDependencies ++= dependencies
  )

}

  ____________________________

The Build.scala file shown in the Solution is equivalent to the following build.sbt file:

name := "SbtExample"

version := "0.2"

scalaVersion := "2.10.0"

scalacOptions += "-deprecation"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"



_______________________________


  // dd settings to Build.settings for sbt to find, and they are automatically build-scoped.
  // add settings to Project.settings for sbt to find, and they are automatically project-scoped.

import sbt._
import Keys._
object HelloBuild extends Build {
 val sampleKeyA = SettingKey[String]("sample-a", "demo key A")
 val sampleKeyB = SettingKey[String]("sample-b", "demo key B")
 val sampleKeyC = SettingKey[String]("sample-c", "demo key C")
 val sampleKeyD = SettingKey[String]("sample-d", "demo key D")
 override lazy val settings = super.settings ++
 Seq(sampleKeyA := "A: in Build.settings in Build.scala", resolvers := Seq())
 lazy val root = Project(id = "hello",
 base = file("."),
 settings = Project.defaultSettings ++ Seq(sampleKeyB := "B: in the root project settings in Build.scala"))
}
// Note that sample-b is scoped to the project ({file:/home/hp/checkout/hello/}hello) rather than the entire
build ({file:/home/hp/checkout/hello/}).



Now, create hello/build.sbt as follows:
sampleKeyC in ThisBuild := "C: in build.sbt scoped to ThisBuild"
sampleKeyD := "D: in build.sbt"

// The settings in .sbt files are project-scoped unless you explicitly specify another scope.
// As you've probably guessed, inspect sample-d matches sample-b:



//  That is, sampleKeyC in ThisBuild in
a .sbt file is equivalent to placing a setting in the Build.settings list in a .scala file. sbt takes build-scoped
settings from both places to create the build definition.



  */