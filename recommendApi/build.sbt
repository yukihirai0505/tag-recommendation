name := "recommendApi"
version := "1.0"

scalaVersion := "2.11.8"

assemblyOutputPath in assembly := file("./api-standalone.jar")

libraryDependencies ++= {
  val sparkV = "2.1.0"
  val akkaV = "10.0.7"
  val scalaTestV = "3.0.1"
  val circeV = "0.6.1"
  Seq(

    "commons-codec" % "commons-codec" % "1.10",

    "com.typesafe.akka" %% "akka-http-core" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaV,
    "de.heikoseeberger" %% "akka-http-circe" % "1.11.0",

    "io.circe" %% "circe-core" % circeV,
    "io.circe" %% "circe-generic" % circeV,
    "io.circe" %% "circe-parser" % circeV,
    "org.apache.spark" %% "spark-core" % sparkV,
    "org.apache.spark" %% "spark-mllib" % sparkV,

    "org.scalatest" %% "scalatest" % scalaTestV % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test"
  )
}

Revolver.settings
enablePlugins(JavaAppPackaging)
