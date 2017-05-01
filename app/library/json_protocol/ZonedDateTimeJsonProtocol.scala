package library.json_protocol

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat, deserializationError}

object ZonedDateTimeJsonProtocol extends DefaultJsonProtocol {
  implicit object DateTimeJsonFormat extends RootJsonFormat[ZonedDateTime] {
    private lazy val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    def write(datetime: ZonedDateTime): JsValue = JsString(datetime.format(formatter))
    def read(json: JsValue): ZonedDateTime = json match {
      case JsString(x) => ZonedDateTime.parse(x, formatter)
      case x           => deserializationError("Expected DateTime as JsString, but got " + x)
    }
  }
}
