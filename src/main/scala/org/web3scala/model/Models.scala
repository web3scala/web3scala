package org.web3scala.model

import scala.collection.immutable.HashMap

final case class Request(jsonrpc: String = "2.0", method: String, var params: List[String] = List.empty[String], id: Int = 1)

trait Response
final case class GenericResponse(jsonrpc: String, id: Int, error: Option[ErrorContent], result: Any) extends Response

final case class SuccessString(jsonrpc: String, id: Int, result: String) extends Response
final case class SuccessBoolean(jsonrpc: String, id: Int, result: Boolean) extends Response
final case class SuccessMap(jsonrpc: String, id: Int, result: HashMap[_,_]) extends Response
final case class SuccessList(jsonrpc: String, id: Int, result: List[_]) extends Response

final case class Error(jsonrpc: String, id: Int, error: ErrorContent) extends Response

final case class ErrorContent(code: Int, message: String) {
  override def toString: String = s"Error/code=$code/message=$message]"
}

trait Block
final case class BlockName(value: String) extends Block {
  val values = List("earliest", "latest", "pending")
  def isValid: Boolean = values.contains(value.toLowerCase)
}
final case class BlockNumber(value: Int) extends Block