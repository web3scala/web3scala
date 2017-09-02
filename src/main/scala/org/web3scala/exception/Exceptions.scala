package org.web3scala.exception

class EthereumException(message: String) extends Exception(message) {
  def this(message: String, cause: Throwable) {
    this(message)
    initCause(cause)
  }
  def this(cause: Throwable) {
    this(Option(cause).map(_.toString).orNull, cause)
  }
  def this() {
    this(null: String)
  }
}

object EthereumException {
  def unapply(e: EthereumException): Option[(String,Throwable)] = Some((e.getMessage, e.getCause))
}

class QuantityEncodingException(message: String) extends EthereumException(message)
class QuantityDecodingException(message: String) extends EthereumException(message)
class InvalidBlockName extends EthereumException