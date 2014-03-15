name := "powah"

version := "1.0-SNAPSHOT"

scalacOptions ++= Seq("-feature")

libraryDependencies ++= Seq(
  jdbc,
  "com.google.inject" % "guice" % "3.0",
  "com.tzavellas" % "sse-guice" % "0.7.1",
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.seleniumhq.selenium" % "selenium-server" % "2.33.0" % "test",
  "com.github.t3hnar" % "scala-bcrypt_2.10" % "2.2",
  "com.typesafe.slick" %% "slick" % "2.0.0",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.5",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.1",
  "org.mockito" % "mockito-all" % "1.9.5"
)

play.Project.playScalaSettings
