name := "sparkSaunaSuite"

version := "0.1"

scalaVersion := "2.12.7"

val sparkVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.1"

//resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
//libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.3"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.10"
