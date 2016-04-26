/*
import sbt._
import Keys._
import bintray.BintrayPlugin.autoImport._

object ExampleBuild extends Build {

  val dependencies = Seq(
    "org.scalatest" %% "scalatest" % "2.2.6" % "test",
    "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
    "ch.qos.logback" % "logback-classic" % "1.1.3" % "test"
  )

  def macroDependencies(version: String): Seq[ModuleID] = {
    val left: Seq[ModuleID] = Seq(
      "org.scala-lang" % "scala-reflect" % version % "provided",
      "org.scala-lang" % "scala-compiler" % version % "provided"
    )
    val right: Seq[ModuleID] = if (version startsWith "2.10.") {
      Seq(compilerPlugin("org.scalamacros" % s"paradise" % "2.0.0" cross CrossVersion.full),
        "org.scalamacros" %% s"quasiquotes" % "2.0.0")
    }
    else {
      Seq()
    }
    left ++ right
  }

  override lazy val settings = super.settings

  lazy val root = Project(id = "scala-trace-debug", base = file("."),
    settings =  Project.defaultSettings ++ Seq(

      organization <<= name,

      scalaVersion := "2.11.7",

      version := "1.2.10",

      crossScalaVersions := Seq("2.10.4", "2.10.5", "2.10.6", "2.11.0", "2.11.1", "2.11.2", "2.11.3", "2.11.4", "2.11.5", "2.11.6", "2.11.7", "2.11.8"),

      resolvers += Resolver.sonatypeRepo("releases"),

      unmanagedSourceDirectories in Compile ++= {
        if (scalaVersion.value startsWith "2.10.") {
          Seq(baseDirectory.value / "src" / "main" / "scala-2.10")
        }
        else {
          Seq(baseDirectory.value / "src" / "main" / "scala-2.11")
        }
      },

      scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-Yinline-warnings", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-nullary-unit"),

      libraryDependencies ++= dependencies ++ macroDependencies(scalaVersion.value),

      bintrayReleaseOnPublish in ThisBuild := true,

      bintrayOmitLicense := false,

      licenses +=("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0")),

      bintrayPackageLabels := Seq("debug", "scala", "trace", "debugging", "assert"),

      bintrayVcsUrl := Some("git@github.com:JohnReedLOL/scala-trace-debug.git")

    )

  )

}
*/