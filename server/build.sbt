name := "powah"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

scalacOptions ++= Seq("-feature")

libraryDependencies ++= Seq(
  jdbc,
  "com.google.inject" % "guice" % "3.0",
  "com.tzavellas" % "sse-guice" % "0.7.1",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.seleniumhq.selenium" % "selenium-server" % "2.33.0" % "test",
  "com.github.t3hnar" % "scala-bcrypt_2.10" % "2.2",
  "joda-time" % "joda-time" % "2.4",
  "org.joda" % "joda-convert" % "1.6",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0",
  "org.mockito" % "mockito-all" % "1.9.5"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
