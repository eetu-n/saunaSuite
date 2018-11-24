package api

import scalaj.http.{Http, HttpRequest, HttpResponse}
import play.api.libs.json._

object Api {

  val url: String = "https://apigtw.vaisala.com/hackjunction2018/saunameasurements/"
  val headers: Map[String, String] = Map("Accept" -> "application/json")
  val method: String = "GET"

  def getHttp(urlAppend: String): HttpRequest =
    Http(url + urlAppend)
      .method(method)
      .headers(headers)

  private def httpExecute(request: HttpRequest): HttpResponse[JsValue] =
    request
      .execute(parser = { inputStream =>
        Json.parse(inputStream)
      })

  def getDevice(device: String, limit: Int = 1): HttpResponse[JsValue] =
    httpExecute {
      getHttp("latest")
        .param("SensorID", device)
        .param("limit", limit.toString)
    }

  def getHistory(device: String, after: Long, before: Long): HttpResponse[JsValue] =
    httpExecute {
      getHttp("history")
        .param("SensorID", device)
        .param("after", after.toString)
        .param("before", before.toString)
    }

}
