name := """csv-reader"""
organization := "com.jeetkunecoder"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"
val alpakkaVersion = "1.1.1"
val akkaHttpVersion = "10.1.9"
val slickVersion = "5.0.0-M5"
val postgreSqlVersion = "42.2.6"
val hikariVersion = "3.3.1"
val catsVersion = "1.3.1"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % alpakkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.play" %% "play-slick" % slickVersion,
  "com.typesafe.play" %% "play-slick-evolutions" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % hikariVersion,
  "org.postgresql" % "postgresql" % postgreSqlVersion,
  "org.typelevel" %% "cats-effect" % catsVersion,
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
)
