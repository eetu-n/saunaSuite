package spark

case class Vaisalytics(
  Measurements: Map[String, Double],
  Units: Map[String, String],
  Timestamp: Long
) {
  def getMeasurementValue(measurement: String): Option[Double] =
    Measurements.get(measurement)
  def getMeasurementUnit(measurement: String): Option[String] =
    Units.get(measurement)

  def getValue(measurement: String): Double =
    getMeasurementValue(measurement).get
  def getUnit(measurement: String): String =
    getMeasurementUnit(measurement).get
}

case class VaisalaMeasurement(
  sensor: String,
  analytics: Vaisalytics
)
