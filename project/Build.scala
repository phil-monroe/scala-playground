import sbt.Keys._
import sbt._

object Build extends sbt.Build {
  name := "scala-playground"
  version := "1.0"
  scalaVersion := "2.10.4"

  lazy val scalaDropwizard = Project("scala-dropwizard", file("scala-dropwizard"),
    settings = Seq(
      libraryDependencies ++= Seq(
        "io.dropwizard" % "dropwizard-core" % "0.7.1",
        "com.massrelevance" %% "dropwizard-scala" % "0.7.1"
      )
    )
  ).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

  lazy val scalaKafka = Project("scala-kafka", file("scala-kafka"),
    settings = Seq(
      libraryDependencies ++= Seq(
        "org.apache.kafka" % "kafka_2.10" % "0.8.1.1" exclude("javax.jms", "jms") exclude("com.sun.jdmk", "jmxtools") exclude("com.sun.jmx", "jmxri"),
        "org.slf4j" % "slf4j-log4j12" % "1.7.7"
      )
    )
  )

  lazy val scalaFinagle = Project("scala-finagle", file("scala-finagle"),
    settings = Seq(
    fullResolvers := TaskKey[Seq[Resolver]]("full-resolvers", "my full"),
      libraryDependencies ++= Seq(
        "com.twitter" % "finagle-http_2.10" % "6.20.0"
      )
    )
  )

}