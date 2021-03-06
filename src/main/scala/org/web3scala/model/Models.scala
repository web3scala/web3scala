package org.web3scala.model

import org.json4s.JsonAST.JValue
import scala.concurrent.Future

case class GenericRequest(jsonrpc: String = "2.0", method: String, var params: AnyRef = List.empty[String], id: Int = 1)
case class GenericResponse(jsonrpc: String, id: Int, error: Option[ErrorContent], result: Option[Any])

trait RequestObject
case class EthSendTransactionObject(from: String, to: Option[String], gas: Option[String], gasPrice: Option[String],
                                    value: Option[String], data: String, nonce: Option[String]) extends RequestObject
case class EthCallObject(from: Option[String], to: String, gas: Option[String], gasPrice: Option[String],
                         value: Option[String], data: Option[String]) extends RequestObject
case class EthEstimateGasObject(from: Option[String], to: String, gas: Option[String], gasPrice: Option[String],
                                value: Option[String], data: Option[String]) extends RequestObject
case class EthNewFilterObject(fromBlock: Option[String], toBlock: Option[String],
                              address: Option[String], topics: Option[List[_]]) extends RequestObject
case class ShhNewMessageFilterObject(symKeyID: Option[String], pubKey: Option[String], key: Option[String],
                                     sig: String, pow: Float, minPoW: Float, topics: List[_],
                                     allowP2P: Boolean) extends RequestObject
case class ShhMessageObject(symKeyID: Option[String], pubKey: Option[String], sig: Option[String], ttl: Int,
                            topics: List[_], padding: String, payload: String, powTime: Int, powTarget: Float,
                            targetPeer: Option[String]) extends RequestObject

trait Response
case class Web3ClientVersion(jsonrpc: String, id: Int, result: String) extends Response
case class Web3Sha3(jsonrpc: String, id: Int, result: String) extends Response
case class NetVersion(jsonrpc: String, id: Int, result: Int) extends Response
case class NetListening(jsonrpc: String, id: Int, result: Boolean) extends Response
case class NetPeerCount(jsonrpc: String, id: Int, result: Int) extends Response
case class EthProtocolVersion(jsonrpc: String, id: Int, result: Int) extends Response
case class EthSyncingFalse(jsonrpc: String, id: Int, result: Boolean) extends Response
case class EthSyncingTrue(jsonrpc: String, id: Int, result: Syncing) extends Response
case class EthCoinbase(jsonrpc: String, id: Int, result: String) extends Response
case class EthMining(jsonrpc: String, id: Int, result: Boolean) extends Response
case class EthHashrate(jsonrpc: String, id: Int, result: Long) extends Response
case class EthGasPrice(jsonrpc: String, id: Int, result: Long) extends Response
case class EthAccounts(jsonrpc: String, id: Int, result: List[_]) extends Response
case class EthBlockNumber(jsonrpc: String, id: Int, result: Long) extends Response
case class EthBalance(jsonrpc: String, id: Int, result: BigInt) extends Response
case class EthStorage(jsonrpc: String, id: Int, result: String) extends Response
case class EthTransactionCount(jsonrpc: String, id: Int, result: Long) extends Response
case class EthBlockTransactionCount(jsonrpc: String, id: Int, result: Long) extends Response
case class EthUncleCount(jsonrpc: String, id: Int, result: Long) extends Response
case class EthCode(jsonrpc: String, id: Int, result: String) extends Response
case class EthSign(jsonrpc: String, id: Int, result: String) extends Response
case class EthSendTransaction(jsonrpc: String, id: Int, result: String) extends Response
case class EthCall(jsonrpc: String, id: Int, result: String) extends Response
case class EthEstimatedGas(jsonrpc: String, id: Int, result: Long) extends Response
case class EthBlock(jsonrpc: String, id: Int, result: Option[Block]) extends Response
case class EthTransaction(jsonrpc: String, id: Int, result: Option[Transaction]) extends Response
case class EthTransactionReceipt(jsonrpc: String, id: Int, result: Option[TransactionReceipt]) extends Response
case class EthFilter(jsonrpc: String, id: Int, result: String) extends Response
case class EthUninstallFilter(jsonrpc: String, id: Int, result: Boolean) extends Response
case class EthFilterLogs(jsonrpc: String, id: Int, result: List[_]) extends Response
case class EthWork(jsonrpc: String, id: Int, result: List[_]) extends Response
case class EthSubmitWork(jsonrpc: String, id: Int, result: Boolean) extends Response
case class EthSubmitHashrate(jsonrpc: String, id: Int, result: Boolean) extends Response
case class ShhVersion(jsonrpc: String, id: Int, result: BigDecimal) extends Response
case class ShhInfo(jsonrpc: String, id: Int, result: ShhInfoDetails) extends Response
case class ShhNewSymKey(jsonrpc: String, id: Int, result: String) extends Response
case class ShhMessageFilter(jsonrpc: String, id: Int, result: String) extends Response
case class ShhPost(jsonrpc: String, id: Int, result: Boolean) extends Response





case class AsyncResponse(future: Future[JValue]) extends Response

case class Error(jsonrpc: String, id: Int, error: ErrorContent) extends Response
case class ErrorContent(code: Int, message: String) {
  override def toString: String = s"Error/code=$code/message=$message]"
}

case class ShhInfoDetails(memory: Int, messages: Int, minPow: Float, maxMessageSize: Int)

case class FilterLogs(logs: List[FilterLog])
case class FilterLog(removed: Boolean, logIndex: Int, transactionIndex: Int, transactionHash: String,
                     blockHash: String, blockNumber: Long, address: String, data: String, topics: List[String])

case class TransactionReceipt(transactionHash: String, transactionIndex: Int, blockHash: String, blockNumber: Long,
                              root: String, logsBloom: String, from: String, to: String,
                              cumulativeGasUsed: Long, gasUsed: Long, contractAddress: String, logs: List[FilterLog])
case class Transaction(hash: String, nonce: Long, blockHash: String, blockNumber: Long, transactionIndex: Long,
                       from: String, to: String, value: Long, gasPrice: Long, gas: Long, input: String)

trait Block
case class BlockWithoutTransactions(number: Long, hash: String, parentHash: String, mixHash: String, nonce: BigInt,
                                    transactionsRoot: String, stateRoot: String, receiptsRoot: String, sha3Uncles: String,
                                    logsBloom: String, miner: String, difficulty: BigInt, totalDifficulty: String,
                                    extraData: String, size: Long, gasLimit: Long, gasUsed: Long, timestamp: Long,
                                    transactions: List[String], uncles: List[String]) extends Block
case class BlockWithTransactions(number: Long, hash: String, parentHash: String, mixHash: String, nonce: BigInt,
                                 transactionsRoot: String, stateRoot: String, receiptsRoot: String, sha3Uncles: String,
                                 logsBloom: String, miner: String, difficulty: BigInt, totalDifficulty: String,
                                 extraData: String, size: Long, gasLimit: Long, gasUsed: Long, timestamp: Long,
                                 transactions: List[BlockTransaction], uncles: List[String]) extends Block
case class BlockTransaction(s: String, blockHash: String, nonce: Long, gasPrice: Long, gas: Long,
                            to: String, v: Long, hash: String, from: String, blockNumber: Long,
                            r: String, value: Long, input: String, transactionIndex: Long)

case class Syncing(pulledStates: Int, knownStates: Int, currentBlock: Int, highestBlock: Int, startingBlock: Int)

trait BlockType
case class BlockName(value: String) extends BlockType {
  val values = List("earliest", "latest", "pending")
  def isValid: Boolean = values.contains(value.toLowerCase)
}
case class BlockNumber(value: Int) extends BlockType