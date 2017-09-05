package org.web3scala

import org.web3scala.exception.InvalidBlockName
import org.web3scala.http.{DispatchHttpClient, JValueHttpClient}
import org.web3scala.json.{JacksonJsonMapper, JsonMapper}
import org.web3scala.model._
import org.web3scala.protocol.Ethereum
import org.web3scala.util.Utils

import scala.collection.immutable.HashMap

class Service(jsonMapper: JsonMapper = new JacksonJsonMapper,
              httpClient: JValueHttpClient = new DispatchHttpClient
             ) extends Ethereum {

  override def web3ClientVersion: Response = {
    val request = Request(method = "web3_clientVersion")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => Web3ClientVersion(response.jsonrpc, response.id, response.result.asInstanceOf[String])
    }
  }
  override def web3Sha3(data: String): Response = {
    val request = Request(method = "web3_sha3", params = data :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => Web3Sha3(response.jsonrpc, response.id, response.result.asInstanceOf[String])
    }
  }
  override def netVersion: Response = {
    val request = Request(method = "net_version")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => NetVersion(response.jsonrpc, response.id, response.result.asInstanceOf[String].toInt)
    }
  }
  override def netListening: Response = {
    val request = Request(method = "net_listening")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => NetListening(response.jsonrpc, response.id, response.result.asInstanceOf[Boolean])
    }
  }
  override def netPeerCount: Response = {
    val request = Request(method = "net_peerCount")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => NetPeerCount(response.jsonrpc, response.id, Utils.hex2int(response.result.asInstanceOf[String]))
    }
  }
  override def ethProtocolVersion: Response = {
    val request = Request(method = "eth_protocolVersion")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthProtocolVersion(response.jsonrpc, response.id, Utils.hex2int(response.result.asInstanceOf[String]))
    }
  }
  override def ethSyncing: Response = {
    val request = Request(method = "eth_syncing")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        response.result match {
          case b: Boolean => EthSyncingFalse(response.jsonrpc, response.id, b)
          case m: HashMap[_,_] =>
            val map = m.map {
              x => (x._1.asInstanceOf[String], Utils.hex2long(x._2.asInstanceOf[String]))
            }
            EthSyncingTrue(response.jsonrpc, response.id, map)
        }
    }
  }
  override def ethCoinbase: Response = {
    val request = Request(method = "eth_coinbase")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthCoinbase(response.jsonrpc, response.id, response.result.asInstanceOf[String])
    }
  }
  override def ethMining: Response = {
    val request = Request(method = "eth_mining")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthMining(response.jsonrpc, response.id, response.result.asInstanceOf[Boolean])
    }
  }
  override def ethHashrate: Response = {
    val request = Request(method = "eth_hashrate")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthHashrate(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethGasPrice: Response = {
    val request = Request(method = "eth_gasPrice")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthGasPrice(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethAccounts: Response = {
    val request = Request(method = "eth_accounts")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthAccounts(response.jsonrpc, response.id, response.result.asInstanceOf[List[String]])
    }
  }
  override def ethBlockNumber: Response = {
    val request = Request(method = "eth_blockNumber")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthBlockNumber(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethGetBalance(address: String, defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getBalance", params = address :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthBalance(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethGetStorageAt(address: String, position: String, defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getStorageAt", params = address :: position :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthStorage(response.jsonrpc, response.id, response.result.asInstanceOf[String])
    }
  }
  override def ethGetTransactionCount(address: String, defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getTransactionCount", params = address :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthTransactionCount(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethGetBlockTransactionCountByHash(blockHash: String): Response = {
    val request = Request(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthBlockTransactionCount(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethGetBlockTransactionCountByNumber(defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthBlockTransactionCount(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethGetUncleCountByBlockHash(blockHash: String): Response = {
    val request = Request(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthUncleCount(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethGetUncleCountByBlockNumber(defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthUncleCount(response.jsonrpc, response.id, Utils.hex2long(response.result.asInstanceOf[String]))
    }
  }
  override def ethGetCode(address: String, defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getCode", params = address :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthCode(response.jsonrpc, response.id, response.result.asInstanceOf[String])
    }
  }
  override def ethSign(address: String, message: String): Response = {
    val request = Request(method = "eth_sign", params = address :: message :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthSign(response.jsonrpc, response.id, response.result.asInstanceOf[String])
    }
  }


  override def asyncWeb3ClientVersion: AsyncResponse = {
    val rq = Request(method = "web3_clientVersion")
    executeAsync(rq)
  }
  override def asyncWeb3Sha3(data: String): AsyncResponse = {
    val rq = Request(method = "web3_sha3", params = data :: Nil)
    executeAsync(rq)
  }
  override def asyncNetVersion: AsyncResponse = {
    val rq = Request(method = "net_version")
    executeAsync(rq)
  }
  override def asyncNetListening: AsyncResponse = {
    val rq = Request(method = "net_listening")
    executeAsync(rq)
  }
  override def asyncNetPeerCount: AsyncResponse = {
    val rq = Request(method = "net_peerCount")
    executeAsync(rq)
  }
  override def asyncEthProtocolVersion: AsyncResponse = {
    val rq = Request(method = "eth_protocolVersion")
    executeAsync(rq)
  }
  override def asyncEthSyncing: AsyncResponse = {
    val rq = Request(method = "eth_syncing")
    executeAsync(rq)
  }
  def asyncEthCoinbase: AsyncResponse = {
    val rq = Request(method = "eth_coinbase")
    executeAsync(rq)
  }
  def asyncEthMining: AsyncResponse = {
    val rq = Request(method = "eth_mining")
    executeAsync(rq)
  }
  def asyncEthHashrate: AsyncResponse = {
    val rq = Request(method = "eth_hashrate")
    executeAsync(rq)
  }
  def asyncEthGasPrice: AsyncResponse = {
    val rq = Request(method = "eth_gasPrice")
    executeAsync(rq)
  }
  def asyncEthAccounts: AsyncResponse = {
    val rq = Request(method = "eth_accounts")
    executeAsync(rq)
  }
  def asyncEthBlockNumber: AsyncResponse = {
    val rq = Request(method = "eth_blockNumber")
    executeAsync(rq)
  }
  def asyncEthGetBalance(address: String, defaultBlock: Block): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getBalance", params = address :: block :: Nil)
    executeAsync(rq)
  }
  def asyncEthGetStorageAt(address: String, position: String, defaultBlock: Block): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getStorageAt", params = address :: position:: block :: Nil)
    executeAsync(rq)
  }
  def asyncEthGetTransactionCount(address: String, defaultBlock: Block): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getTransactionCount", params = address :: block :: Nil)
    executeAsync(rq)
  }
  def asyncEthGetBlockTransactionCountByHash(blockHash: String): AsyncResponse = {
    val rq = Request(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    executeAsync(rq)
  }
  def asyncEthGetBlockTransactionCountByNumber(defaultBlock: Block): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    executeAsync(rq)
  }
  def asyncEthGetUncleCountByBlockHash(blockHash: String): AsyncResponse = {
    val rq = Request(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    executeAsync(rq)
  }
  def asyncEthGetUncleCountByBlockNumber(defaultBlock: Block): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    executeAsync(rq)
  }
  def asyncEthGetCode(address: String, defaultBlock: Block): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getCode", params = address :: block :: Nil)
    executeAsync(rq)
  }
  def asyncEthSign(address: String, message: String): AsyncResponse = {
    val rq = Request(method = "eth_sign", params = address :: message :: Nil)
    executeAsync(rq)
  }

  import org.web3scala.json.JacksonReaders._

  private def executeAsync(request: Request): AsyncResponse = {
    val requestAsBytes = jsonMapper.writeAsBytes(request)
    AsyncResponse(httpClient.async(requestAsBytes))
  }
  private def executeSync(request: Request): GenericResponse = {
    val requestAsBytes = jsonMapper.writeAsBytes(request)
    val response = httpClient.sync(requestAsBytes)
    response.as[GenericResponse]
  }
}
object Service {
  @throws(classOf[InvalidBlockName])
  def blockValue(block: Block): String = {
    block match {
      case name: BlockName =>
        if (name.isValid) name.value.toLowerCase
        else throw new InvalidBlockName
      case number: BlockNumber =>
        Utils.long2hex(number.value)
    }
  }
}