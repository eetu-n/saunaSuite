package spark

case class Vaisalytics(
  Measurements: Map[String, Double],
  Units: Map[String, String],
  Timestamp: Long
)
