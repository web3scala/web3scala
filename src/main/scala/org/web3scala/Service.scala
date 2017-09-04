package org.web3scala

import org.json4s.DefaultFormats
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
    val rq = Request(method = "web3_clientVersion")
    executeSync(rq)
  }
  override def web3Sha3(data: String): Response = {
    val rq = Request(method = "web3_sha3", params = data :: Nil)
    executeSync(rq)
  }
  override def netVersion: Response = {
    val rq = Request(method = "net_version")
    executeSync(rq)
  }
  override def netListening: Response = {
    val rq = Request(method = "net_listening")
    executeSync(rq)
  }
  override def netPeerCount: Response = {
    val rq = Request(method = "net_peerCount")
    executeSync(rq)
  }
  override def ethProtocolVersion: Response = {
    val rq = Request(method = "eth_protocolVersion")
    executeSync(rq)
  }
  override def ethSyncing: Response = {
    val rq = Request(method = "eth_syncing")
    executeSync(rq)
  }
  override def ethCoinbase: Response = {
    val rq = Request(method = "eth_coinbase")
    executeSync(rq)
  }
  override def ethMining: Response = {
    val rq = Request(method = "eth_mining")
    executeSync(rq)
  }
  override def ethHashrate: Response = {
    val rq = Request(method = "eth_hashrate")
    executeSync(rq)
  }
  override def ethGasPrice: Response = {
    val rq = Request(method = "eth_gasPrice")
    executeSync(rq)
  }
  override def ethAccounts: Response = {
    val rq = Request(method = "eth_accounts")
    executeSync(rq)
  }
  override def ethBlockNumber: Response = {
    val rq = Request(method = "eth_blockNumber")
    executeSync(rq)
  }
  override def ethGetBalance(address: String, defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getBalance", params = address :: block :: Nil)
    executeSync(rq)
  }
  override def ethGetStorageAt(address: String, position: String, defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getStorageAt", params = address :: position :: block :: Nil)
    executeSync(rq)
  }
  override def ethGetTransactionCount(address: String, defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getTransactionCount", params = address :: block :: Nil)
    executeSync(rq)
  }
  override def ethGetBlockTransactionCountByHash(blockHash: String): Response = {
    val rq = Request(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    executeSync(rq)
  }
  override def ethGetBlockTransactionCountByNumber(defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    executeSync(rq)
  }
  override def ethGetUncleCountByBlockHash(blockHash: String): Response = {
    val rq = Request(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    executeSync(rq)
  }
  override def ethGetUncleCountByBlockNumber(defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    executeSync(rq)
  }
  override def ethGetCode(address: String, defaultBlock: Block): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getCode", params = address :: block :: Nil)
    executeSync(rq)
  }
  override def ethSign(address: String, message: String): Response = {
    val rq = Request(method = "eth_sign", params = address :: message :: Nil)
    executeSync(rq)
  }


  def asyncWeb3ClientVersion: AsyncResponse = {
    val rq = Request(method = "web3_clientVersion")
    executeAsync(rq)
  }
  def asyncWeb3Sha3(data: String): AsyncResponse = {
    val rq = Request(method = "web3_sha3", params = data :: Nil)
    executeAsync(rq)
  }
  def asyncNetVersion: AsyncResponse = {
    val rq = Request(method = "net_version")
    executeAsync(rq)
  }
  def asyncNetListening: AsyncResponse = {
    val rq = Request(method = "net_listening")
    executeAsync(rq)
  }
  def asyncNetPeerCount: AsyncResponse = {
    val rq = Request(method = "net_peerCount")
    executeAsync(rq)
  }
  def asyncEthProtocolVersion: AsyncResponse = {
    val rq = Request(method = "eth_protocolVersion")
    executeAsync(rq)
  }
  def asyncEthSyncing: AsyncResponse = {
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


  implicit val formats: DefaultFormats.type = DefaultFormats
  import org.web3scala.json.JacksonReaders._

  private def executeAsync(request: Request): AsyncResponse = {
    val requestAsBytes = jsonMapper.writeAsBytes(request)
    AsyncResponse(httpClient.async(requestAsBytes))
  }
  private def executeSync(request: Request): Response = {
    val requestAsBytes = jsonMapper.writeAsBytes(request)
    val response = httpClient.sync(requestAsBytes)
    handleResponse(response.as[GenericResponse])
  }
  def handleResponse(response: GenericResponse): Response = Service.handleResponse(response)
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
  def handleResponse(response: GenericResponse): Response = {
    if (response.error.isDefined)
      Error(response.jsonrpc, response.id, response.error.get)
    else
      response.result match {
        case m: HashMap[_,_] => SuccessMap(response.jsonrpc, response.id, m)
        case l: List[_] => SuccessList(response.jsonrpc, response.id, l)
        case s: String => SuccessString(response.jsonrpc, response.id, s)
        case b: Boolean => SuccessBoolean(response.jsonrpc, response.id, b)
      }
  }
}