package api

import play.api.libs.json._
import scalaj.http.HttpResponse
import spark.{VaisalaMeasurement, Vaisalytics}

object Format {

  type JsPairs = Map[String, JsValue]

  private def getJsonMeasurements(jsValue: JsValue): (Map[String, Double], Map[String, String]) = {
    import scala.collection.mutable.Map
    val measurements = Map[String, Double]()
    val units = Map[String, String]()

    jsValue match {
      case JsObject(underlying) =>
        underlying.foreach({ case (key, value) =>
          value \ "value" get match {
            case JsNumber(num) =>
              measurements(key) = num.toDouble
          }
          value \ "unit" get match {
            case JsString(str) =>
              units(key) = str
          }
        })
    }

    (measurements.toMap, units.toMap)
  }

  private def getJsonTimestamp(jsValue: JsValue): Long =
    jsValue match {
      case JsNumber(value) => value.toLongExact
    }

  def convertJsPair(jsPair: JsPairs): Vaisalytics = {
    val jsonMeasurements = jsPair("Measurements")
    val (measurements, units) = getJsonMeasurements(jsonMeasurements)

    val timestamp = getJsonTimestamp(jsPair("Timestamp"))

    Vaisalytics(measurements, units, timestamp)
  }

  def extractJsPairs(jsValue: JsValue): List[JsPairs] = {
    var res: List[JsPairs] = Nil

    def inner(jsValue: JsValue): Unit =
      jsValue match {
        case JsArray(value) => value foreach inner
        case JsObject(underlying) =>
          res = res :+ underlying.toMap
      }

    inner(jsValue)
    res
  }

  def convertFromJsValue(jsValue: JsValue): List[Vaisalytics] =
    extractJsPairs(jsValue)
      .map(convertJsPair)

  def convertDeviceHttpResponse(device: String, limit: Int = 1): List[Vaisalytics] =
    convertDeviceHttpResponse(Api.getDevice(device, limit))

  def convertDeviceHttpResponse(response: HttpResponse[JsValue]): List[Vaisalytics] =
    convertFromJsValue(response.body)

  def convertHistoryHttpRequest(device: String, after: Long, before: Long): List[Vaisalytics] =
    convertDeviceHttpResponse(Api.getHistory(device, after, before))

  def convertHistoryHttpRequest(response: HttpResponse[JsValue]): Vector[Vaisalytics] = {
    response.body match {
      case JsArray(seq) =>
        seq.flatMap(value => extractJsPairs(value).map(convertJsPair)).toVector
    }
  }

  def convertToVaisalaMeasurement(sensor: String, analytics: Seq[Vaisalytics]): Seq[VaisalaMeasurement] =
    analytics
      .map(analytic => VaisalaMeasurement(sensor, analytic))

}
