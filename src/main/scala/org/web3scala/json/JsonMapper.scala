package org.web3scala.json

trait JsonMapper {

  /** Serializes AnyRef value into a byte array */
  def writeAsBytes(value: AnyRef): Array[Byte]
}