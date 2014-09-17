name := "domain-events-lite"

version := "0.1"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.1.1",
  "com.h2database" % "h2" % "1.4.181",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "joda-time" % "joda-time" % "2.3",
  "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test")

