name := "scala-trace-debug"

organization := "scala.trace"

scalaVersion := "2.11.7"

version := "2.2.13"

crossScalaVersions := Seq("2.10.4", "2.11.7")

resolvers += Resolver.sonatypeRepo("releases")

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
  "ch.qos.logback" % "logback-classic" % "1.1.3" % "test",
  "junit" % "junit" % "4.11" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test
    exclude("junit", "junit-dep")
)

libraryDependencies ++= macroDependencies(scalaVersion.value)

scalacOptions ++= Seq("-unchecked", "-feature", "-Xlint", "-Yinline-warnings", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-nullary-unit")

bintrayReleaseOnPublish in ThisBuild := true

bintrayOmitLicense := false

licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0"))

bintrayPackageLabels := Seq("debug", "scala", "trace", "debugging", "assert")

bintrayVcsUrl := Some("git@github.com:JohnReedLOL/scala-trace-debug.git")