name := """prrr"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "mysql" % "mysql-connector-java" % "5.1.41",
  "org.scalikejdbc"      %% "scalikejdbc-play-initializer"   % "2.5.+",
  "org.skinny-framework" %% "skinny-orm" % "2.3.+",
  "io.spray"             %% "spray-json"      % "1.3.3",
  "org.skinny-framework" %% "skinny-http-client" % "2.3.6",
  "org.slf4j" % "slf4j-simple" % "1.7.25",
  "org.flywaydb" %% "flyway-play" % "3.0.1"
)
