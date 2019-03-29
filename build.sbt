
name := "pdk-bot"

version := "1.0"

scalaVersion := "2.12.4"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")
//javacOptions ++= Seq("-encoding", "UTF-8")
scalacOptions += "-target:jvm-1.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test
//libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"
//libraryDependencies += "com.google.code.gson" % "gson" % "2.8.2"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.7"



test in assembly := {}
assemblyJarName in assembly := s"../../data/pdk-bot-${org.apache.commons.lang3.time.DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd.HHmm")}.jar"
mainClass in assembly := Some("bot.framework.demo.Demo")


libraryDependencies  ++= Seq(
  // Last stable release
  "org.scalanlp" %% "breeze" % "0.13.2",

  // Native libraries are not included by default. add this if you want them (as of 0.7)
  // Native libraries greatly improve performance, but increase jar sizes.
  // It also packages various blas implementations, which have licenses that may or may not
  // be compatible with the Apache License. No GPL code, as best I know.
  "org.scalanlp" %% "breeze-natives" % "0.13.2",

  // The visualization library is distributed separately as well.
  // It depends on LGPL code
  "org.scalanlp" %% "breeze-viz" % "0.13.2"
)


resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"