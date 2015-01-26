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
      libraryDependencies ++= Seq(
        "com.twitter" % "finagle-http_2.10" % "6.20.0"
      )
    )
  )

  lazy val scalaColossus = Project("scala-colossus", file("scala-colossus"),
    settings = Seq(
      libraryDependencies ++= Seq(
        "com.tumblr" %% "colossus" % "0.5.0"
      )
    )
  )

  lazy val scalaBenchmark = Project("scala-benchmark", file("scala-benchmark"),
    settings = Seq(
      libraryDependencies ++= Seq(
        "org.glassfish.jersey.core" % "jersey-client" % "2.13",
        "org.apache.httpcomponents" % "httpclient" % "4.3.6",
        "io.dropwizard.metrics" % "metrics-core" % "3.1.0",
        "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.1.3",
        "org.glassfish.jersey.core" % "jersey-client" % "2.15",
        "org.glassfish.jersey.connectors" % "jersey-apache-connector" % "2.15"

      )
    )
  )

  lazy val scalaCurator = Project("scala-curator", file("scala-curator"),
    settings = Seq(
      libraryDependencies ++= Seq(
        "org.apache.curator" % "curator-framework" % "2.6.0",
        "org.slf4j" % "slf4j-log4j12" % "1.7.7"
      )
    )
  )

  lazy val scalaSpark = Project("scala-spark", file("scala-spark"))
    .settings(defaultSettings: _*)
    .settings(
      Seq(
        libraryDependencies ++= Seq(
          "org.apache.spark" %% "spark-core" % V.spark,
          "org.apache.hadoop" % "hadoop-client" % V.hadoop
//          "org.apache.spark" %% "spark-yarn" % V.spark
        ),


        externalResolvers := Seq(
//          Resolver.mavenLocal,
          DefaultMavenRepository
        )
      ): _*
    )


  def defaultSettings =
    net.virtualvoid.sbt.graph.Plugin.graphSettings

}

object V {
  val hadoop = "2.3.0"
  val spark = "1.1.0"
}