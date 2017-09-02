package org.web3scala.json

trait JsonMapper {
  def writeAsBytes(value: AnyRef): Array[Byte]
}