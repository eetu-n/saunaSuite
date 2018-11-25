package utils

import java.io.FileInputStream

import api.Format
import play.api.libs.json.{JsValue, Json}
import spark.Vaisalytics

object SaunaJson {

  def readJsonFile(path: String): JsValue = {
    val stream = new FileInputStream(path)
    try {
      Json.parse(stream)
    } finally {
      stream.close()
    }
  }

  def readVaisalyticsFile(path: String): List[Vaisalytics] =
    Format.convertFromJsValue(readJsonFile(path))

}
