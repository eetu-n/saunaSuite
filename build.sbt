name := "sparkSaunaSuite"

version := "0.1"

scalaVersion := "2.12.7"

val sparkVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.1"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.10"

val breezeVersion = "0.13.2"

libraryDependencies  ++= Seq(
  "org.scalanlp" %% "breeze" % breezeVersion,
  "org.scalanlp" %% "breeze-natives" % breezeVersion,
  "org.scalanlp" %% "breeze-viz" % breezeVersion
)

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

