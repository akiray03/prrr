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
  "org.skinny-framework" %% "skinny-orm" % "2.3.+"
)
