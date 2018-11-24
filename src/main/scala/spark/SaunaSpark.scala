package spark

import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}


class SaunaSpark(implicit spark: SparkSession) extends java.io.Serializable {

  // https://stackoverflow.com/questions/51374630/how-to-traverse-through-a-schema-in-spark
  def traverseRow(row: Row): (Map[String, Double], Map[String, String]) = {
    val measurements = scala.collection.mutable.Map[String, Double]()
    val units = scala.collection.mutable.Map[String, String]()

    def traverse(schema: StructType, prefix: String): Unit = {
      schema.fields.foreach {
        case StructField(name, inner: StructType, _, _) =>
          traverse(inner, if (prefix.isEmpty) name else s"$prefix.$name")
        case StructField(_, _: DoubleType, _, _) =>
          measurements += prefix -> row.getStruct(row.fieldIndex(prefix)).getDouble(1)
        case StructField(_, _: LongType, _, _) =>
          measurements += prefix -> row.getStruct(row.fieldIndex(prefix)).getLong(1)
        case StructField(_, _: StringType, _, _) =>
          units += prefix -> row.getStruct(row.fieldIndex(prefix)).getString(0)
      }
    }

    traverse(row.schema, "")
    (measurements.toMap, units.toMap)
  }

  private def convertToMeasurement(df: DataFrame): Dataset[Vaisalytics] = {
    import spark.implicits._
    val measurementIndex = df.columns.indexOf("Measurements")
    val timestampIndex = df.columns.indexOf("Timestamp")

    df map { row =>
      val (measurements, units) = traverseRow(row.getStruct(measurementIndex))
      Vaisalytics(
        measurements,
        units,
        row.getAs[Long](timestampIndex)
      )
    }
  }

  def run(path: String): Dataset[Vaisalytics] = {
    val input = spark.read.json(path)
    convertToMeasurement(input)
  }

}

object SaunaSpark {
  def apply()(implicit sc: SparkSession): SaunaSpark =
    new SaunaSpark()
}
