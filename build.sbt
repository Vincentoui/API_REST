name := """play-scala-call_dat_visualisation"""
organization := "XiVo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.11"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  "org.playframework.anorm" %% "anorm-postgres" % "2.7.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "org.playframework.anorm" %% "anorm" % "2.7.0",
  "org.postgresql" % "postgresql" % "42.3.5"
)
