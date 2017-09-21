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
    val rq = GenericRequest(method = "web3_clientVersion")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => Web3ClientVersion(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
    }
  }
  override def web3Sha3(data: String): Response = {
    val rq = GenericRequest(method = "web3_sha3", params = data :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => Web3Sha3(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
    }
  }
  override def netVersion: Response = {
    val rq = GenericRequest(method = "net_version")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => NetVersion(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String].toInt)
    }
  }
  override def netListening: Response = {
    val rq = GenericRequest(method = "net_listening")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => NetListening(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[Boolean])
    }
  }
  override def netPeerCount: Response = {
    val rq = GenericRequest(method = "net_peerCount")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => NetPeerCount(rs.jsonrpc, rs.id, Utils.hex2int(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethProtocolVersion: Response = {
    val rq = GenericRequest(method = "eth_protocolVersion")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthProtocolVersion(rs.jsonrpc, rs.id, Utils.hex2int(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethSyncing: Response = {
    val rq = GenericRequest(method = "eth_syncing")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        rs.result.get match {
          case b: Boolean => EthSyncingFalse(rs.jsonrpc, rs.id, b)
          case m: HashMap[_,_] =>
            val map = m.map {
              x => (x._1.asInstanceOf[String], Utils.hex2long(x._2.asInstanceOf[String]))
            }
            EthSyncingTrue(rs.jsonrpc, rs.id, map)
        }
    }
  }
  override def ethCoinbase: Response = {
    val rq = GenericRequest(method = "eth_coinbase")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthCoinbase(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
    }
  }
  override def ethMining: Response = {
    val rq = GenericRequest(method = "eth_mining")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthMining(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[Boolean])
    }
  }
  override def ethHashrate: Response = {
    val rq = GenericRequest(method = "eth_hashrate")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthHashrate(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGasPrice: Response = {
    val rq = GenericRequest(method = "eth_gasPrice")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthGasPrice(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethAccounts: Response = {
    val rq = GenericRequest(method = "eth_accounts")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthAccounts(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[List[String]])
    }
  }
  override def ethBlockNumber: Response = {
    val rq = GenericRequest(method = "eth_blockNumber")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthBlockNumber(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetBalance(address: String, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBalance", params = address :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthBalance(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetStorageAt(address: String, position: String, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getStorageAt", params = address :: position :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthStorage(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
    }
  }
  override def ethGetTransactionCount(address: String, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getTransactionCount", params = address :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthTransactionCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetBlockTransactionCountByHash(blockHash: String): Response = {
    val rq = GenericRequest(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthBlockTransactionCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetBlockTransactionCountByNumber(defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthBlockTransactionCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetUncleCountByBlockHash(blockHash: String): Response = {
    val rq = GenericRequest(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthUncleCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetUncleCountByBlockNumber(defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthUncleCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetCode(address: String, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getCode", params = address :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthCode(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
    }
  }
  override def ethSign(address: String, message: String): Response = {
    val rq = GenericRequest(method = "eth_sign", params = address :: message :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthSign(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
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
    val rq = GenericRequest(method = "eth_sendTransaction", params = params :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthTransactionHash(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
    }
  }
  override def ethSendRawTransaction(signedTransactionData: String): Response = {
    val rq = GenericRequest(method = "eth_sendRawTransaction", params = signedTransactionData :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthTransactionHash(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
    }
  }
  override def ethCall(obj: EthCallObject, defaultBlock: BlockType): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_call", params = obj :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthCall(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String])
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
    val rq = GenericRequest(method = "eth_estimateGas", params = params :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None => EthEstimatedGas(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetBlockByHash(blockHash: String, fullTransactionObjects: Boolean): Response = {
    val rq = GenericRequest(method = "eth_getBlockByHash", params = blockHash :: fullTransactionObjects :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        import org.web3scala.json.JacksonReaders._
        val block = if (fullTransactionObjects)
          Extraction.decompose(rs.result.get).as[BlockWithTransactions]
        else
          Extraction.decompose(rs.result.get).as[BlockWithoutTransactions]
        EthBlockObject(rs.jsonrpc, rs.id, Some(block))
    }
  }
  override def ethGetBlockByNumber(defaultBlock: BlockType, fullTransactionObjects: Boolean): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBlockByNumber", params = block :: fullTransactionObjects :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        import org.web3scala.json.JacksonReaders._
        val block = if (fullTransactionObjects)
          Extraction.decompose(rs.result.get).as[BlockWithTransactions]
        else
          Extraction.decompose(rs.result.get).as[BlockWithoutTransactions]
        EthBlockObject(rs.jsonrpc, rs.id, Some(block))
    }
  }
  override def ethGetTransactionByHash(transactionHash: String): Response = {
    val rq = GenericRequest(method = "eth_getTransactionByHash", params = transactionHash :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            EthTransactionObject(rs.jsonrpc, rs.id, Some(transaction))
          case None => EthTransactionObject(rs.jsonrpc, rs.id, None)
        }
    }
  }
  override def ethGetTransactionByBlockHashAndIndex(blockHash: String, transactionIndex: String): Response = {
    val rq = GenericRequest(method = "eth_getTransactionByBlockHashAndIndex", params = blockHash :: transactionIndex :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            EthTransactionObject(rs.jsonrpc, rs.id, Some(transaction))
          case None => EthTransactionObject(rs.jsonrpc, rs.id, None)
        }
    }
  }
  override def ethGetTransactionByBlockNumberAndIndex(defaultBlock: BlockType, transactionIndex: String): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getTransactionByBlockNumberAndIndex", params = block :: transactionIndex :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            EthTransactionObject(rs.jsonrpc, rs.id, Some(transaction))
          case None => EthTransactionObject(rs.jsonrpc, rs.id, None)
        }
    }
  }
  override def ethGetTransactionReceipt(transactionHash: String): Response = {
    val rq = GenericRequest(method = "eth_getTransactionReceipt", params = transactionHash :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transactionReceipt = Extraction.decompose(result).as[TransactionReceipt]
            EthTransactionReceiptObject(rs.jsonrpc, rs.id, Some(transactionReceipt))
          case None => EthTransactionReceiptObject(rs.jsonrpc, rs.id, None)
        }
    }
  }
  override def ethGetUncleByBlockHashAndIndex(blockHash: String, uncleIndex: String): Response = {
    val rq = GenericRequest(method = "eth_getUncleByBlockHashAndIndex", params = blockHash :: uncleIndex :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val block = Extraction.decompose(result).as[BlockWithoutTransactions]
            EthBlockObject(rs.jsonrpc, rs.id, Some(block))
          case None => EthBlockObject(rs.jsonrpc, rs.id, None)
        }
    }
  }
  override def ethGetUncleByBlockNumberAndIndex(defaultBlock: BlockType, uncleIndex: String): Response = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getUncleByBlockNumberAndIndex", params = block :: uncleIndex :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Error(rs.jsonrpc, rs.id, e)
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val block = Extraction.decompose(result).as[BlockWithoutTransactions]
            EthBlockObject(rs.jsonrpc, rs.id, Some(block))
          case None => EthBlockObject(rs.jsonrpc, rs.id, None)
        }
    }
  }




  override def asyncWeb3ClientVersion: AsyncResponse = {
    val rq = GenericRequest(method = "web3_clientVersion")
    executeAsync(rq)
  }
  override def asyncWeb3Sha3(data: String): AsyncResponse = {
    val rq = GenericRequest(method = "web3_sha3", params = data :: Nil)
    executeAsync(rq)
  }
  override def asyncNetVersion: AsyncResponse = {
    val rq = GenericRequest(method = "net_version")
    executeAsync(rq)
  }
  override def asyncNetListening: AsyncResponse = {
    val rq = GenericRequest(method = "net_listening")
    executeAsync(rq)
  }
  override def asyncNetPeerCount: AsyncResponse = {
    val rq = GenericRequest(method = "net_peerCount")
    executeAsync(rq)
  }
  override def asyncEthProtocolVersion: AsyncResponse = {
    val rq = GenericRequest(method = "eth_protocolVersion")
    executeAsync(rq)
  }
  override def asyncEthSyncing: AsyncResponse = {
    val rq = GenericRequest(method = "eth_syncing")
    executeAsync(rq)
  }
  override def asyncEthCoinbase: AsyncResponse = {
    val rq = GenericRequest(method = "eth_coinbase")
    executeAsync(rq)
  }
  override def asyncEthMining: AsyncResponse = {
    val rq = GenericRequest(method = "eth_mining")
    executeAsync(rq)
  }
  override def asyncEthHashrate: AsyncResponse = {
    val rq = GenericRequest(method = "eth_hashrate")
    executeAsync(rq)
  }
  override def asyncEthGasPrice: AsyncResponse = {
    val rq = GenericRequest(method = "eth_gasPrice")
    executeAsync(rq)
  }
  override def asyncEthAccounts: AsyncResponse = {
    val rq = GenericRequest(method = "eth_accounts")
    executeAsync(rq)
  }
  override def asyncEthBlockNumber: AsyncResponse = {
    val rq = GenericRequest(method = "eth_blockNumber")
    executeAsync(rq)
  }
  override def asyncEthGetBalance(address: String, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBalance", params = address :: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetStorageAt(address: String, position: String, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getStorageAt", params = address :: position:: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionCount(address: String, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getTransactionCount", params = address :: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetBlockTransactionCountByHash(blockHash: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetBlockTransactionCountByNumber(defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetUncleCountByBlockHash(blockHash: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetUncleCountByBlockNumber(defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetCode(address: String, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getCode", params = address :: block :: Nil)
    executeAsync(rq)
  }
  override def asyncEthSign(address: String, message: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_sign", params = address :: message :: Nil)
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
    val rq = GenericRequest(method = "eth_sendTransaction", params = params :: Nil)
    executeAsync(rq)
  }
  override def asyncEthSendRawTransaction(data: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_sendRawTransaction", params = data :: Nil)
    executeAsync(rq)
  }
  override def asyncEthCall(obj: EthCallObject, defaultBlock: BlockType): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_call", params = obj :: block :: Nil)
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
    val rq = GenericRequest(method = "eth_estimateGas", params = params :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetBlockByHash(blockHash: String, fullTransactionObjects: Boolean): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getBlockByHash", params = blockHash :: fullTransactionObjects :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetBlockByNumber(defaultBlock: BlockType, fullTransactionObjects: Boolean): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBlockByNumber", params = block :: fullTransactionObjects :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionByHash(transactionHash: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getTransactionByHash", params = transactionHash :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionByBlockHashAndIndex(blockHash: String, transactionIndex: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getTransactionByBlockHashAndIndex", params = blockHash :: transactionIndex :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionByBlockNumberAndIndex(defaultBlock: BlockType, transactionIndex: String): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getTransactionByBlockNumberAndIndex", params = block :: transactionIndex :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetTransactionReceipt(transactionHash: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getTransactionReceipt", params = transactionHash :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetUncleByBlockHashAndIndex(blockHash: String, uncleIndex: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getUncleByBlockHashAndIndex", params = blockHash :: uncleIndex :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetUncleByBlockNumberAndIndex(defaultBlock: BlockType, uncleIndex: String): AsyncResponse = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getUncleByBlockNumberAndIndex", params = block :: uncleIndex :: Nil)
    executeAsync(rq)
  }


  import org.web3scala.json.JacksonReaders._

  private def executeAsync(rq: GenericRequest): AsyncResponse = {
    val rqAsBytes = jsonMapper.writeAsBytes(rq)
    AsyncResponse(httpClient.async(rqAsBytes))
  }
  private def executeSync(rq: GenericRequest): GenericResponse = {
    val rqAsBytes = jsonMapper.writeAsBytes(rq)
    val rs = httpClient.sync(rqAsBytes)
    rs.as[GenericResponse]
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