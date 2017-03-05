name := "scala-trace-debug"

organization := "com.github.johnreedlol"

description := "Macro based print debugging. Locates log statements in your IDE."

developers := List(Developer(id = "johnreedlol", name = "John-Michael Reed", email = "johnmichaelreedfas@gmail.com", new URL("https://github.com/JohnReedLOL")))

scmInfo := Some(ScmInfo(new URL("https://github.com/JohnReedLOL/scala-trace-debug"),
  "scm:git:git://github.com/JohnReedLOL/scala-trace-debug.git",
  None))

pomExtra := (
  <url>https://github.com/JohnReedLOL/scala-trace-debug</url>
  )

scalaVersion := "2.11.7"

version := "5.0.0" // For compatibility, only use first two digits (MajorVersion, MinorVersion)

crossScalaVersions := Seq("2.10.6", "2.11.7", "2.12.0")

resolvers += Resolver.sonatypeRepo("releases")

pgpReadOnly := false //  To import a key

useGpg := true // The first step towards using the GPG command line tool is to make sbt-pgp gpg-aware. (skip for built-in Bouncy Castle PGP implementation)

def macroDependencies(version: String) =
  Seq(
    "org.scala-lang" % "scala-reflect" % version % "provided",
    "org.scala-lang" % "scala-compiler" % version % "provided"
  ) ++
    (if (version startsWith "2.10.")
      Seq(compilerPlugin("org.scalamacros" % s"paradise" % "2.1.0" cross CrossVersion.full),
        "org.scalamacros" %% s"quasiquotes" % "2.1.0")
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

libraryDependencies ++= macroDependencies(scalaVersion.value)

// libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11"      % "1.0.4"

scalacOptions ++= Seq("-unchecked", "-feature", "-Xlint", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-nullary-unit")

bintrayReleaseOnPublish in ThisBuild := true

bintrayOmitLicense := false

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayPackageLabels := Seq("debug", "scala", "trace", "debugging", "assert")

bintrayVcsUrl := Some("git@github.com:JohnReedLOL/scala-trace-debug.git")