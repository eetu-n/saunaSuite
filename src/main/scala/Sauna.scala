import org.apache.spark._
import org.apache.spark.sql.{SQLContext, SparkSession}
import spark.SaunaSpark

object Sauna {

  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      println("No input file given!")
      return
    }

    val sparkConf =
      new SparkConf()
        .setAppName("SaunaSuite")
        .setMaster("local")

    val sparkContext = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sparkContext)

    implicit val spark: SparkSession = sqlContext.sparkSession
    val saunaSpark = SaunaSpark()
    saunaSpark.run(args.head)

    println("Main function over and out.")
  }

}
