package org.web3scala

import org.json4s.Extraction
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
      case None => Web3ClientVersion(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def web3Sha3(data: String): Response = {
    val request = Request(method = "web3_sha3", params = data :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => Web3Sha3(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def netVersion: Response = {
    val request = Request(method = "net_version")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => NetVersion(response.jsonrpc, response.id, response.result.get.asInstanceOf[String].toInt)
    }
  }
  override def netListening: Response = {
    val request = Request(method = "net_listening")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => NetListening(response.jsonrpc, response.id, response.result.get.asInstanceOf[Boolean])
    }
  }
  override def netPeerCount: Response = {
    val request = Request(method = "net_peerCount")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => NetPeerCount(response.jsonrpc, response.id, Utils.hex2int(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethProtocolVersion: Response = {
    val request = Request(method = "eth_protocolVersion")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthProtocolVersion(response.jsonrpc, response.id, Utils.hex2int(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethSyncing: Response = {
    val request = Request(method = "eth_syncing")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        response.result.get match {
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
      case None => EthCoinbase(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def ethMining: Response = {
    val request = Request(method = "eth_mining")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthMining(response.jsonrpc, response.id, response.result.get.asInstanceOf[Boolean])
    }
  }
  override def ethHashrate: Response = {
    val request = Request(method = "eth_hashrate")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthHashrate(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGasPrice: Response = {
    val request = Request(method = "eth_gasPrice")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthGasPrice(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethAccounts: Response = {
    val request = Request(method = "eth_accounts")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthAccounts(response.jsonrpc, response.id, response.result.get.asInstanceOf[List[String]])
    }
  }
  override def ethBlockNumber: Response = {
    val request = Request(method = "eth_blockNumber")
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthBlockNumber(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetBalance(address: String, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getBalance", params = address :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthBalance(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetStorageAt(address: String, position: String, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getStorageAt", params = address :: position :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthStorage(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def ethGetTransactionCount(address: String, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getTransactionCount", params = address :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthTransactionCount(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetBlockTransactionCountByHash(blockHash: String): Response = {
    val request = Request(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthBlockTransactionCount(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetBlockTransactionCountByNumber(defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthBlockTransactionCount(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetUncleCountByBlockHash(blockHash: String): Response = {
    val request = Request(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthUncleCount(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetUncleCountByBlockNumber(defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthUncleCount(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetCode(address: String, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getCode", params = address :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthCode(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def ethSign(address: String, message: String): Response = {
    val request = Request(method = "eth_sign", params = address :: message :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthSign(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def ethSendTransaction(from: String, to: Option[String], gas: Option[String], gasPrice: Option[String],
                                  value: Option[String], data: String, nonce: Option[String]): Response = {
    val params = HashMap(
      "from" -> from,
      "to" -> to,
      "gas" -> gas,
      "gasPrice" -> gasPrice,
      "value" -> value,
      "data" -> data,
      "nonce" -> nonce
    )
    val request = Request(method = "eth_sendTransaction", params = params :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthTransactionHash(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def ethSendRawTransaction(signedTransactionData: String): Response = {
    val request = Request(method = "eth_sendRawTransaction", params = signedTransactionData :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthTransactionHash(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def ethCall(from: Option[String], to: String, gas: Option[String], gasPrice: Option[String],
                       value: Option[String], data: Option[String], defaultBlock: BlockType): Response = {
    val params = HashMap(
      "from" -> from,
      "to" -> to,
      "gas" -> gas,
      "gasPrice" -> gasPrice,
      "value" -> value,
      "data" -> data
    )
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_call", params = params :: block :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthCall(response.jsonrpc, response.id, response.result.get.asInstanceOf[String])
    }
  }
  override def ethEstimateGas(from: Option[String], to: String, gas: Option[String], gasPrice: Option[String],
                              value: Option[String], data: Option[String]): Response = {
    val params = HashMap(
      "from" -> from,
      "to" -> to,
      "gas" -> gas,
      "gasPrice" -> gasPrice,
      "value" -> value,
      "data" -> data
    )
    val request = Request(method = "eth_estimateGas", params = params :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None => EthEstimatedGas(response.jsonrpc, response.id, Utils.hex2long(response.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetBlockByHash(blockHash: String, fullTransactionObjects: Boolean): Response = {
    val request = Request(method = "eth_getBlockByHash", params = blockHash :: fullTransactionObjects :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        import org.web3scala.json.JacksonReaders._
        val block = if (fullTransactionObjects)
          Extraction.decompose(response.result.get).as[BlockWithTransactions]
        else
          Extraction.decompose(response.result.get).as[BlockWithoutTransactions]
        EthBlockObject(response.jsonrpc, response.id, Some(block))
    }
  }
  override def ethGetBlockByNumber(defaultBlock: BlockType, fullTransactionObjects: Boolean): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getBlockByNumber", params = block :: fullTransactionObjects :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        import org.web3scala.json.JacksonReaders._
        val block = if (fullTransactionObjects)
          Extraction.decompose(response.result.get).as[BlockWithTransactions]
        else
          Extraction.decompose(response.result.get).as[BlockWithoutTransactions]
        EthBlockObject(response.jsonrpc, response.id, Some(block))
    }
  }
  override def ethGetTransactionByHash(transactionHash: String): Response = {
    val request = Request(method = "eth_getTransactionByHash", params = transactionHash :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        response.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            EthTransactionObject(response.jsonrpc, response.id, Some(transaction))
          case None => EthTransactionObject(response.jsonrpc, response.id, None)
        }
    }
  }
  override def ethGetTransactionByBlockHashAndIndex(blockHash: String, transactionIndex: String): Response = {
    val request = Request(method = "eth_getTransactionByBlockHashAndIndex", params = blockHash :: transactionIndex :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        response.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            EthTransactionObject(response.jsonrpc, response.id, Some(transaction))
          case None => EthTransactionObject(response.jsonrpc, response.id, None)
        }
    }
  }
  override def ethGetTransactionByBlockNumberAndIndex(defaultBlock: BlockType, transactionIndex: String): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getTransactionByBlockNumberAndIndex", params = block :: transactionIndex :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        response.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            EthTransactionObject(response.jsonrpc, response.id, Some(transaction))
          case None => EthTransactionObject(response.jsonrpc, response.id, None)
        }
    }
  }
  override def ethGetTransactionReceipt(transactionHash: String): Response = {
    val request = Request(method = "eth_getTransactionReceipt", params = transactionHash :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        response.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transactionReceipt = Extraction.decompose(result).as[TransactionReceipt]
            EthTransactionReceiptObject(response.jsonrpc, response.id, Some(transactionReceipt))
          case None => EthTransactionReceiptObject(response.jsonrpc, response.id, None)
        }
    }
  }
  override def ethGetUncleByBlockHashAndIndex(blockHash: String, uncleIndex: String): Response = {
    val request = Request(method = "eth_getUncleByBlockHashAndIndex", params = blockHash :: uncleIndex :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        response.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val block = Extraction.decompose(result).as[BlockWithoutTransactions]
            EthBlockObject(response.jsonrpc, response.id, Some(block))
          case None => EthBlockObject(response.jsonrpc, response.id, None)
        }
    }
  }
  override def ethGetUncleByBlockNumberAndIndex(defaultBlock: BlockType, uncleIndex: String): Response = {
    val block = Service.blockValue(defaultBlock)
    val request = Request(method = "eth_getUncleByBlockNumberAndIndex", params = block :: uncleIndex :: Nil)
    val response = executeSync(request)
    response.error match {
      case Some(e) => Error(response.jsonrpc, response.id, e)
      case None =>
        response.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val block = Extraction.decompose(result).as[BlockWithoutTransactions]
            EthBlockObject(response.jsonrpc, response.id, Some(block))
          case None => EthBlockObject(response.jsonrpc, response.id, None)
        }
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
  override def asyncEthCoinbase: AsyncResponse = {
    val rq = Request(method = "eth_coinbase")
    executeAsync(rq)
  }
  override def asyncEthMining: AsyncResponse = {
    val rq = Request(method = "eth_mining")
    executeAsync(rq)
  }
  override def asyncEthHashrate: AsyncResponse = {
    val rq = Request(method = "eth_hashrate")
    executeAsync(rq)
  }
  override def asyncEthGasPrice: AsyncResponse = {
    val rq = Request(method = "eth_gasPrice")
    executeAsync(rq)
  }
  override def asyncEthAccounts: AsyncResponse = {
    val rq = Request(method = "eth_accounts")
    executeAsync(rq)
  }
  override def asyncEthBlockNumber: AsyncResponse = {
    val rq = Request(method = "eth_blockNumber")
    executeAsync(rq)
  }
  override def asyncEthGetBalance(address: String, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getBalance", params = address :: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetStorageAt(address: String, position: String, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getStorageAt", params = address :: position:: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionCount(address: String, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getTransactionCount", params = address :: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetBlockTransactionCountByHash(blockHash: String): AsyncResponse = {
    val rq = Request(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetBlockTransactionCountByNumber(defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetUncleCountByBlockHash(blockHash: String): AsyncResponse = {
    val rq = Request(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetUncleCountByBlockNumber(defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetCode(address: String, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getCode", params = address :: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthSign(address: String, message: String): AsyncResponse = {
    val rq = Request(method = "eth_sign", params = address :: message :: Nil)
    executeAsync(rq)
  }
  override def asyncEthSendTransaction(from: String, to: Option[String], gas: Option[String],
                                       gasPrice: Option[String], value: Option[String], data: String,
                                       nonce: Option[String]): AsyncResponse = {
    val params = HashMap(
      "from" -> from,
      "to" -> to,
      "gas" -> gas,
      "gasPrice" -> gasPrice,
      "value" -> value,
      "data" -> data,
      "nonce" -> nonce
    )
    val rq = Request(method = "eth_sendTransaction", params = params :: Nil)
    executeAsync(rq)
  }
  override def asyncEthSendRawTransaction(data: String): AsyncResponse = {
    val rq = Request(method = "eth_sendRawTransaction", params = data :: Nil)
    executeAsync(rq)
  }
  override def asyncEthCall(from: Option[String], to: String, gas: Option[String], gasPrice: Option[String],
                            value: Option[String], data: Option[String], defaultBlock: BlockType): AsyncResponse = {
    val params = HashMap(
      "from" -> from,
      "to" -> to,
      "gas" -> gas,
      "gasPrice" -> gasPrice,
      "value" -> value,
      "data" -> data
    )
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_call", params = params :: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthEstimateGas(from: Option[String], to: String, gas: Option[String], gasPrice: Option[String],
                                   value: Option[String], data: Option[String]): AsyncResponse = {
    val params = HashMap(
      "from" -> from,
      "to" -> to,
      "gas" -> gas,
      "gasPrice" -> gasPrice,
      "value" -> value,
      "data" -> data
    )
    val rq = Request(method = "eth_estimateGas", params = params :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetBlockByHash(blockHash: String, fullTransactionObjects: Boolean): AsyncResponse = {
    val rq = Request(method = "eth_getBlockByHash", params = blockHash :: fullTransactionObjects :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetBlockByNumber(defaultBlock: BlockType, fullTransactionObjects: Boolean): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getBlockByNumber", params = block :: fullTransactionObjects :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionByHash(transactionHash: String): AsyncResponse = {
    val rq = Request(method = "eth_getTransactionByHash", params = transactionHash :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionByBlockHashAndIndex(blockHash: String, transactionIndex: String): AsyncResponse = {
    val rq = Request(method = "eth_getTransactionByBlockHashAndIndex", params = blockHash :: transactionIndex :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionByBlockNumberAndIndex(defaultBlock: BlockType, transactionIndex: String): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getTransactionByBlockNumberAndIndex", params = block :: transactionIndex :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionReceipt(transactionHash: String): AsyncResponse = {
    val rq = Request(method = "eth_getTransactionReceipt", params = transactionHash :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetUncleByBlockHashAndIndex(blockHash: String, uncleIndex: String): AsyncResponse = {
    val rq = Request(method = "eth_getUncleByBlockHashAndIndex", params = blockHash :: uncleIndex :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetUncleByBlockNumberAndIndex(defaultBlock: BlockType, uncleIndex: String): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = Request(method = "eth_getUncleByBlockNumberAndIndex", params = block :: uncleIndex :: Nil)
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
  def blockValue(defaultBlock: BlockType): String = {
    defaultBlock match {
      case name: BlockName =>
        if (name.isValid) name.value.toLowerCase
        else throw new InvalidBlockName
      case number: BlockNumber =>
        Utils.long2hex(number.value)
    }
  }
}