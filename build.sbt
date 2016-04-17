name := "scala-trace-debug"

organization := "scala-trace-debug"

scalaVersion := "2.11.7"

version := "0.2.2"

crossScalaVersions := Seq("2.10.4", "2.11.7")

resolvers += Resolver.sonatypeRepo("releases")

/** Error during compilation of source code that depends on macros that access the source code:
  *
  * [info] Setting version to 2.10.4
  * exception during macro expansion:
  * [error] java.lang.UnsupportedOperationException: Position.start on class scala.reflect.internal.util.OffsetPosition
  * [error]         at scala.reflect.internal.util.Position.start(Position.scala:114)
  *
  * As a workaround, you can pass -Dsbt.parser.simple=true to your play/SBT command line. This will revert to the old .sbt parser that splits based on \n\n.
  */

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

// baseDirectory.value / ".."/"shared" / "src" / "main" / "scala-2.11"

unmanagedSourceDirectories in Compile ++= {
  if (scalaVersion.value startsWith "2.10.") {
    Seq(baseDirectory.value / "src"/ "main" / "scala-2.10")
  }
  else {
    Seq(baseDirectory.value / "src" / "main" / "scala-2.11")
  }
}

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % "test"
)

libraryDependencies ++= macroDependencies(scalaVersion.value)

// to debug macros, use -Ymacro-debug-lite. IGNORE DEPRECATION WARNING "method startOrPoint in trait Position is deprecated" FROM SCALA 2.10 -

scalacOptions ++= Seq("-unchecked", "-feature", "-Xlint", "-Yinline-warnings", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-nullary-unit")

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