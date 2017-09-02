package org.web3scala.json

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

class JacksonJsonMapper extends JsonMapper {
  override def writeAsBytes(value: AnyRef): Array[Byte] = {
    JacksonJsonMapper.mapper.writeValueAsBytes(value)
  }
}
object JacksonJsonMapper {
  private val mapperInstance = new ObjectMapper with ScalaObjectMapper
  mapperInstance.registerModule(DefaultScalaModule)
  mapperInstance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  def mapper: ObjectMapper with ScalaObjectMapper = mapperInstance
}