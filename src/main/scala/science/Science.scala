package science

import java.util.Calendar

import api.Format
import breeze.linalg.DenseVector
import breeze.optimize.ApproximateGradientFunction
import spark.Vaisalytics
import breeze.stats.distributions._
import utils.SaunaJson

object Science {

  private def getNow: Long =
    Calendar
      .getInstance
      .getTimeInMillis

  def foldDifferences(analytics: Seq[Double]) =
    (analytics zip analytics.tail)
      .map({ case (f, s) => f - s})

  private def updateSequence(arr: Array[Double]): Unit = {
    var i = arr.length - 2
    while (i >= 0) {
      arr(i) += arr(i + 1)
      i -= 1
    }
  }

  /*
  private def hasPeak(arr: Array[Double], freq: Int, growth: Double = 3.0) = {
    require(arr.size > 0)
    val count = (arr zip arr.tail)
      .count({ case (f, s) => s >= f + growth })
    println(count)
    count >= freq
  }
  */
  private def hasPeak(
           analytics: Seq[Vaisalytics],
           measurement: String,
           index: Int,
           windowSize: Int,
           freq: Int,
           growth: Double = 0.5
  ): Boolean = {
    require(index >= 0)
    require(index < analytics.size)
    require(index + windowSize < analytics.size)

    val check: Int => Boolean = i =>
      analytics(i + 1).getValue(measurement) >= analytics(i).getValue(measurement) + growth

    var count = 0
    var i = index
    while (i < windowSize - 1) {
      println(s"${analytics(i+1).getValue(measurement)}:${analytics(i).getValue(measurement)}")
      if (check(i)) {
        count += 1
      }
      i += 1
    }

    println(count)
    count >= freq
  }

  def naivePeakFinder(
             analytics: Seq[Vaisalytics],
             measurement: String,
             freq: Int = 3,
             windowSize: Int = 30
  ): (List[Seq[Vaisalytics]], Int) = {
    require(windowSize > 0)
    //val changes = new Array[Double](windowSize)
    var peaks: List[Seq[Vaisalytics]] = Nil

    var i = 0
    while (i < analytics.size - windowSize) {
      //changes(windowSize - 1) += analytics(i).getValue(measurement)
      //updateSequence(changes)
      //if (hasPeak(changes, freq)) {
      if (hasPeak(analytics, measurement, i, windowSize, freq)) {
        peaks ::= analytics.slice(i, i + windowSize)
        i += windowSize - 1
      }
      i += 1
    }

    (peaks, windowSize)
  }

  def findPeak(analytics: Seq[Vaisalytics], measurement: String, windowSize: Int = 30) = {
    val values = DenseVector(analytics.map(_.getValue(measurement)): _*)
    val wholeMV = breeze.stats.meanAndVariance(values)

    var peaks: List[DenseVector[Vaisalytics]] = Nil

    var i = 0
    while (i < analytics.size - windowSize) {
      val window = values(i until windowSize + i)
      val mv = breeze.stats.meanAndVariance(window)
      println(mv + " " + mv.stdDev)
      i += 1
    }

    println()
    println(wholeMV)
    println(wholeMV.variance)
    println(wholeMV.count)
    println(wholeMV.mean)
    println(wholeMV.stdDev)
  }

  def measurementsToDenseVector(analytics: Seq[Vaisalytics], measurement: String): DenseVector[Double] =
    DenseVector(analytics map { _.getValue(measurement) }: _*)

  def derivate(analytics: Seq[Vaisalytics], measurement: String, windowSize: Int = 10) = {
    //val values = measurementsToDenseVector(analytics, measurement)

    val getMes: Int => Double = i => analytics(i).getValue(measurement)
    val ans = new Array[Double](analytics.size)

    var i = 1
    while (i < analytics.size) {
      val curr = analytics(i)
      val mes = getMes(i)
      val angle = getMes(i-1) - mes

      ans(i) = 60 * angle + mes - 22

      i += 1
    }

//    def h(anal: Vaisalytics): Double =
//      anal.Timestamp + 60 *
//    def g(vec: DenseVector[Double]) = vec.sum
//    val diffG = new ApproximateGradientFunction(g)

    ans
  }

  def main(args: Array[String]): Unit = {
    val ts = getNow
    val window = 10000000
    //val t = 1000 * 60 * 60 * 105
    val t = 0//1000 * 60 * 60 * 2

    //val tmp = SaunaJson.readVaisalyticsFile("src/main/resources/test.json")
    //val vals = Format.convertHistoryHttpRequest("Ceiling1", ts - window - t, ts - t).grouped(300).map(_.head).toArray
    val vals = Format.convertDeviceHttpResponse("Ceiling1", 620)
    val tmp = vals.grouped(300).map(_.head).toArray
    val measurement = "Temperature"
    //findPeak(tmp, measurement)
    val res = derivate(tmp, "Temperature")
    (res zip tmp).foreach({ case (r, v) => println(s"derivative $r : temperature ${v.getValue("Temperature")}") })
    //val tmp = Format.convertDeviceHttpResponse("Ceiling1", 216000)

    /*
    val tmp = Format.convertHistoryHttpRequest("Ceiling1", ts - window - t, ts - t)
    val (peaks, _) = naivePeakFinder(tmp, "Relative humidity")
    println(peaks)
    peaks.foreach(println)
    foldDifferences(tmp.map(_.getValue("Relative humidity"))).foreach(println)
    */

    //tmp.foreach(i => println(i.Timestamp))
    //val t = tmp.map(_.Measurements("Relative humidity"))
    //val folded = foldDifferences(t)
    //(t zip folded).foreach(println)

    //val r = naivePeakFinder(t)
  }

}
