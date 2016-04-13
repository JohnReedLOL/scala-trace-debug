name := "scala-trace-debug"

organization := "scala-trace-debug"

// version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

version := "0.1.9"

// scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.4", "2.11.7")

def macroDependencies(version: String) =
  Seq(
    "org.scala-lang" % "scala-reflect" % version % "provided",
    "org.scala-lang" % "scala-compiler" % version % "provided"
  ) ++
    (if (version startsWith "2.10.")
      Seq(compilerPlugin("org.scalamacros" % s"paradise" % "2.0.0" cross CrossVersion.full),
        "org.scalamacros" %% s"quasiquotes" % "2.0.0")
    else
      Seq())

unmanagedSourceDirectories in Compile ++= {
  if (scalaVersion.value startsWith "2.10.") {System.err.println("baseDirectory.value_2.10: " + baseDirectory.value); Seq(baseDirectory.value / "src"/ "main" / "scala-2.10") }
  else {System.err.println("baseDirectory.value_2.11: " + baseDirectory.value); Seq(baseDirectory.value / "src" / "main" / "scala-2.11") }
}

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
)

libraryDependencies ++= macroDependencies(scalaVersion.value)

// to debug macros, use -Ymacro-debug-lite

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xlint", "-Xfatal-warnings", "-Yinline-warnings", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-nullary-unit")

// initialCommands := "import example._"

// This is needed for macros: (No longer needed due to "org.scala-lang" % "scala-reflect" % version % "provided")

// libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)

// bintray settings

bintrayReleaseOnPublish in ThisBuild := true

bintrayOmitLicense := false

// Apache-2.0

licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0"))

bintrayPackageLabels := Seq("debug", "scala", "trace", "debugging", "assert")

// https://github.com/JohnReedLOL/scala-trace-debug

bintrayVcsUrl := Some("git@github.com:JohnReedLOL/scala-trace-debug.git")

// bintraySyncMavenCentral