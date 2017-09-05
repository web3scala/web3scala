package org.web3scala.json

import org.json4s.{DefaultFormats, JValue, Reader}
import org.web3scala.model.{ErrorContent, GenericResponse}

object JacksonReaders {
  implicit val formats: DefaultFormats.type = DefaultFormats
  implicit object GenericResponseReader extends Reader[GenericResponse] {
    def read(json: JValue): GenericResponse = GenericResponse(
      (json \ "jsonrpc").extract[String],
      (json \ "id").extract[Int],
      (json \ "error").extractOpt[ErrorContent],
      (json \ "result").extract[Any]
    )
  }
  implicit object ErrorContentReader extends Reader[ErrorContent] {
    def read(json: JValue): ErrorContent = ErrorContent(
      (json \ "code").extract[Int],
      (json \ "message").extract[String]
    )
  }
}