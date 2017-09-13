package org.web3scala.model

import org.json4s.JsonAST.JValue
import scala.collection.immutable.HashMap
import scala.concurrent.Future

case class Request(jsonrpc: String = "2.0", method: String, var params: AnyRef = List.empty[String], id: Int = 1)

trait Response
case class GenericResponse(jsonrpc: String, id: Int, error: Option[ErrorContent], result: Any) extends Response
case class Web3ClientVersion(jsonrpc: String, id: Int, result: String) extends Response
case class Web3Sha3(jsonrpc: String, id: Int, result: String) extends Response
case class NetVersion(jsonrpc: String, id: Int, result: Int) extends Response
case class NetListening(jsonrpc: String, id: Int, result: Boolean) extends Response
case class NetPeerCount(jsonrpc: String, id: Int, result: Int) extends Response
case class EthProtocolVersion(jsonrpc: String, id: Int, result: Int) extends Response
case class EthSyncingFalse(jsonrpc: String, id: Int, result: Boolean) extends Response
case class EthSyncingTrue(jsonrpc: String, id: Int, result: HashMap[_,_]) extends Response
case class EthCoinbase(jsonrpc: String, id: Int, result: String) extends Response
case class EthMining(jsonrpc: String, id: Int, result: Boolean) extends Response
case class EthHashrate(jsonrpc: String, id: Int, result: Long) extends Response
case class EthGasPrice(jsonrpc: String, id: Int, result: Long) extends Response
case class EthAccounts(jsonrpc: String, id: Int, result: List[_]) extends Response
case class EthBlockNumber(jsonrpc: String, id: Int, result: Long) extends Response
case class EthBalance(jsonrpc: String, id: Int, result: Long) extends Response
case class EthStorage(jsonrpc: String, id: Int, result: String) extends Response
case class EthTransactionCount(jsonrpc: String, id: Int, result: Long) extends Response
case class EthBlockTransactionCount(jsonrpc: String, id: Int, result: Long) extends Response
case class EthUncleCount(jsonrpc: String, id: Int, result: Long) extends Response
case class EthCode(jsonrpc: String, id: Int, result: String) extends Response
case class EthSign(jsonrpc: String, id: Int, result: String) extends Response
case class EthTransaction(jsonrpc: String, id: Int, result: String) extends Response
case class EthCall(jsonrpc: String, id: Int, result: String) extends Response
case class EthEstimatedGas(jsonrpc: String, id: Int, result: Long) extends Response
case class EthBlock(jsonrpc: String, id: Int, result: Option[Block]) extends Response





case class AsyncResponse(future: Future[JValue]) extends Response

case class Error(jsonrpc: String, id: Int, error: ErrorContent) extends Response
case class ErrorContent(code: Int, message: String) {
  override def toString: String = s"Error/code=$code/message=$message]"
}


case class Transaction(s: String, blockHash: String, nonce: Long, gasPrice: Long, gas: Long,
                       to: String, v: Long, hash: String, from: String, blockNumber: Long,
                       r: String, value: Long, input: String, transactionIndex: Long)

trait Block
case class BlockWithoutTransactions(number: Long, hash: String, parentHash: String, mixHash: String, nonce: String,
                                    transactionsRoot: String, stateRoot: String, receiptsRoot: String, sha3Uncles: String,
                                    logsBloom: String, miner: String, difficulty: Long, totalDifficulty: Long,
                                    extraData: String, size: Long, gasLimit: Long, gasUsed: Long, timestamp: Long,
                                    transactions: List[String], uncles: List[String]) extends Block

case class BlockWithTransactions(number: Long, hash: String, parentHash: String, mixHash: String, nonce: String,
                                 transactionsRoot: String, stateRoot: String, receiptsRoot: String, sha3Uncles: String,
                                 logsBloom: String, miner: String, difficulty: Long, totalDifficulty: Long,
                                 extraData: String, size: Long, gasLimit: Long, gasUsed: Long, timestamp: Long,
                                 transactions: List[Transaction], uncles: List[String]) extends Block

trait BlockType
case class BlockName(value: String) extends BlockType {
  val values = List("earliest", "latest", "pending")
  def isValid: Boolean = values.contains(value.toLowerCase)
}
case class BlockNumber(value: Int) extends BlockType