import sbt._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "powah"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    "com.google.inject" % "guice" % "3.0",
    "com.tzavellas" % "sse-guice" % "0.7.1",
    "com.typesafe.slick" %% "slick" % "1.0.1",
    "com.typesafe.play" %% "play-slick" % "0.3.3",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "org.seleniumhq.selenium" % "selenium-server" % "2.33.0" % "test",
    "com.github.t3hnar" % "scala-bcrypt_2.10" % "2.2"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )
}
