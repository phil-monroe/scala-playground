name := "scala-playground"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka_2.10" % "0.8.1.1" exclude("javax.jms", "jms") exclude("com.sun.jdmk", "jmxtools") exclude("com.sun.jmx", "jmxri"),
  "org.slf4j" % "slf4j-log4j12" % "1.7.7"
)

addCommandAlias("generate-project", ";update-classifiers;update-sbt-classifiers;gen-idea sbt-classifiers")

net.virtualvoid.sbt.graph.Plugin.graphSettings