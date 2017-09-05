package org.web3scala.json

import org.scalatest.{FlatSpec, Matchers}

class JacksonJsonMapperSpec extends FlatSpec with Matchers {
  "JacksonJsonMapper" should "serialize AnyRef value into a byte array" in {
    val result = new JacksonJsonMapper().writeAsBytes("test")
    result.mkString(", ") shouldBe "34, 116, 101, 115, 116, 34"
  }
}