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

  override def web3ClientVersion: Either[Error, Web3ClientVersion] = {
    val rq = GenericRequest(method = "web3_clientVersion")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(Web3ClientVersion(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def web3Sha3(data: String): Either[Error, Web3Sha3] = {
    val rq = GenericRequest(method = "web3_sha3", params = data :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(Web3Sha3(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def netVersion: Either[Error, NetVersion] = {
    val rq = GenericRequest(method = "net_version")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(NetVersion(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String].toInt))
    }
  }
  override def netListening: Either[Error, NetListening] = {
    val rq = GenericRequest(method = "net_listening")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(NetListening(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[Boolean]))
    }
  }
  override def netPeerCount: Either[Error, NetPeerCount] = {
    val rq = GenericRequest(method = "net_peerCount")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(NetPeerCount(rs.jsonrpc, rs.id, Utils.hex2int(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethProtocolVersion: Either[Error, EthProtocolVersion] = {
    val rq = GenericRequest(method = "eth_protocolVersion")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthProtocolVersion(rs.jsonrpc, rs.id, Utils.hex2int(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethSyncing: Either[Error, Response] = {
    val rq = GenericRequest(method = "eth_syncing")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        rs.result.get match {
          case b: Boolean => Right(EthSyncingFalse(rs.jsonrpc, rs.id, b))
          case m: HashMap[_,_] =>
            import org.web3scala.json.JacksonReaders._
            val syncing = Extraction.decompose(m).as[Syncing]
            Right(EthSyncingTrue(rs.jsonrpc, rs.id, syncing))
        }
    }
  }
  override def ethCoinbase: Either[Error, EthCoinbase] = {
    val rq = GenericRequest(method = "eth_coinbase")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthCoinbase(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethMining: Either[Error, EthMining] = {
    val rq = GenericRequest(method = "eth_mining")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthMining(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[Boolean]))
    }
  }
  override def ethHashrate: Either[Error, EthHashrate] = {
    val rq = GenericRequest(method = "eth_hashrate")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthHashrate(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGasPrice: Either[Error, EthGasPrice] = {
    val rq = GenericRequest(method = "eth_gasPrice")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthGasPrice(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethAccounts: Either[Error, EthAccounts] = {
    val rq = GenericRequest(method = "eth_accounts")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthAccounts(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[List[String]]))
    }
  }
  override def ethBlockNumber: Either[Error, EthBlockNumber] = {
    val rq = GenericRequest(method = "eth_blockNumber")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthBlockNumber(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGetBalance(address: String, defaultBlock: BlockType): Either[Error, EthBalance] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBalance", params = address :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthBalance(rs.jsonrpc, rs.id, Utils.hex2bigint(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGetStorageAt(address: String, position: String, defaultBlock: BlockType): Either[Error, EthStorage] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getStorageAt", params = address :: position :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthStorage(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethGetTransactionCount(address: String, defaultBlock: BlockType): Either[Error, EthTransactionCount] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getTransactionCount", params = address :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthTransactionCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGetBlockTransactionCountByHash(blockHash: String): Either[Error, EthBlockTransactionCount] = {
    val rq = GenericRequest(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthBlockTransactionCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGetBlockTransactionCountByNumber(defaultBlock: BlockType): Either[Error, EthBlockTransactionCount] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthBlockTransactionCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGetUncleCountByBlockHash(blockHash: String): Either[Error, EthUncleCount] = {
    val rq = GenericRequest(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthUncleCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGetUncleCountByBlockNumber(defaultBlock: BlockType): Either[Error, EthUncleCount] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthUncleCount(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGetCode(address: String, defaultBlock: BlockType): Either[Error, EthCode] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getCode", params = address :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthCode(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethSign(address: String, message: String): Either[Error, EthSign] = {
    val rq = GenericRequest(method = "eth_sign", params = address :: message :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthSign(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethSendTransaction(obj: EthSendTransactionObject): Either[Error, EthSendTransaction] = {
    val rq = GenericRequest(method = "eth_sendTransaction", params = obj :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthSendTransaction(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethSendRawTransaction(signedTransactionData: String): Either[Error, EthSendTransaction] = {
    val rq = GenericRequest(method = "eth_sendRawTransaction", params = signedTransactionData :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthSendTransaction(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethCall(obj: EthCallObject, defaultBlock: BlockType): Either[Error, EthCall] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_call", params = obj :: block :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthCall(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethEstimateGas(obj: EthEstimateGasObject): Either[Error, EthEstimatedGas] = {
    val rq = GenericRequest(method = "eth_estimateGas", params = obj :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthEstimatedGas(rs.jsonrpc, rs.id, Utils.hex2long(rs.result.get.asInstanceOf[String])))
    }
  }
  override def ethGetBlockByHash(blockHash: String, fullTransactionObjects: Boolean): Either[Error, EthBlock] = {
    val rq = GenericRequest(method = "eth_getBlockByHash", params = blockHash :: fullTransactionObjects :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        import org.web3scala.json.JacksonReaders._
        val block = if (fullTransactionObjects)
          Extraction.decompose(rs.result.get).as[BlockWithTransactions]
        else
          Extraction.decompose(rs.result.get).as[BlockWithoutTransactions]
        Right(EthBlock(rs.jsonrpc, rs.id, Some(block)))
    }
  }
  override def ethGetBlockByNumber(defaultBlock: BlockType, fullTransactionObjects: Boolean): Either[Error, EthBlock] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getBlockByNumber", params = block :: fullTransactionObjects :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        import org.web3scala.json.JacksonReaders._
        val block = if (fullTransactionObjects)
          Extraction.decompose(rs.result.get).as[BlockWithTransactions]
        else
          Extraction.decompose(rs.result.get).as[BlockWithoutTransactions]
        Right(EthBlock(rs.jsonrpc, rs.id, Some(block)))
    }
  }
  override def ethGetTransactionByHash(transactionHash: String): Either[Error, EthTransaction] = {
    val rq = GenericRequest(method = "eth_getTransactionByHash", params = transactionHash :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            Right(EthTransaction(rs.jsonrpc, rs.id, Some(transaction)))
          case None => Right(EthTransaction(rs.jsonrpc, rs.id, None))
        }
    }
  }
  override def ethGetTransactionByBlockHashAndIndex(blockHash: String, transactionIndex: String): Either[Error, EthTransaction] = {
    val rq = GenericRequest(method = "eth_getTransactionByBlockHashAndIndex", params = blockHash :: transactionIndex :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            Right(EthTransaction(rs.jsonrpc, rs.id, Some(transaction)))
          case None => Right(EthTransaction(rs.jsonrpc, rs.id, None))
        }
    }
  }
  override def ethGetTransactionByBlockNumberAndIndex(defaultBlock: BlockType, transactionIndex: String): Either[Error, EthTransaction] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getTransactionByBlockNumberAndIndex", params = block :: transactionIndex :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transaction = Extraction.decompose(result).as[Transaction]
            Right(EthTransaction(rs.jsonrpc, rs.id, Some(transaction)))
          case None => Right(EthTransaction(rs.jsonrpc, rs.id, None))
        }
    }
  }
  override def ethGetTransactionReceipt(transactionHash: String): Either[Error, EthTransactionReceipt] = {
    val rq = GenericRequest(method = "eth_getTransactionReceipt", params = transactionHash :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val transactionReceipt = Extraction.decompose(result).as[TransactionReceipt]
            Right(EthTransactionReceipt(rs.jsonrpc, rs.id, Some(transactionReceipt)))
          case None => Right(EthTransactionReceipt(rs.jsonrpc, rs.id, None))
        }
    }
  }
  override def ethGetUncleByBlockHashAndIndex(blockHash: String, uncleIndex: String): Either[Error, EthBlock] = {
    val rq = GenericRequest(method = "eth_getUncleByBlockHashAndIndex", params = blockHash :: uncleIndex :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val block = Extraction.decompose(result).as[BlockWithoutTransactions]
            Right(EthBlock(rs.jsonrpc, rs.id, Some(block)))
          case None => Right(EthBlock(rs.jsonrpc, rs.id, None))
        }
    }
  }
  override def ethGetUncleByBlockNumberAndIndex(defaultBlock: BlockType, uncleIndex: String): Either[Error, EthBlock] = {
    val block = Service.blockValue(defaultBlock)
    val rq = GenericRequest(method = "eth_getUncleByBlockNumberAndIndex", params = block :: uncleIndex :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        rs.result match {
          case Some(result) =>
            import org.web3scala.json.JacksonReaders._
            val block = Extraction.decompose(result).as[BlockWithoutTransactions]
            Right(EthBlock(rs.jsonrpc, rs.id, Some(block)))
          case None => Right(EthBlock(rs.jsonrpc, rs.id, None))
        }
    }
  }
  override def ethNewFilter(obj: EthNewFilterObject): Either[Error, EthFilter] = {
    val rq = GenericRequest(method = "eth_newFilter", params = obj :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthFilter(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethNewBlockFilter: Either[Error, EthFilter] = {
    val rq = GenericRequest(method = "eth_newBlockFilter")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthFilter(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethNewPendingTransactionFilter: Either[Error, EthFilter] = {
    val rq = GenericRequest(method = "eth_newPendingTransactionFilter")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthFilter(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def ethUninstallFilter(id: String): Either[Error, EthUninstallFilter] = {
    val rq = GenericRequest(method = "eth_uninstallFilter", params = id :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthUninstallFilter(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[Boolean]))
    }
  }
  override def ethGetFilterChanges(id: String): Either[Error, EthFilterLogs] = {
    val rq = GenericRequest(method = "eth_getFilterChanges", params = id :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(processGetFilterLogsResponse(rs))
    }
  }
  override def ethGetFilterLogs(id: String): Either[Error, EthFilterLogs] = {
    val rq = GenericRequest(method = "eth_getFilterLogs", params = id :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(processGetFilterLogsResponse(rs))
    }
  }
  override def ethGetLogs(obj: EthNewFilterObject): Either[Error, EthFilterLogs] = {
    val rq = GenericRequest(method = "eth_getLogs", params = obj :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(processGetFilterLogsResponse(rs))
    }
  }
  override def ethGetWork: Either[Error, EthWork] = {
    val rq = GenericRequest(method = "eth_getWork")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthWork(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[List[String]]))
    }
  }
  override def ethSubmitWork(nonce: String, powHash: String, mixDigest: String): Either[Error, EthSubmitWork] = {
    val rq = GenericRequest(method = "eth_submitWork", params = nonce :: powHash :: mixDigest :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthSubmitWork(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[Boolean]))
    }
  }
  override def ethSubmitHashrate(hashrate: String, clientId: String): Either[Error, EthSubmitHashrate] = {
    val rq = GenericRequest(method = "eth_submitHashrate", params = hashrate :: clientId :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(EthSubmitHashrate(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[Boolean]))
    }
  }
  override def shhVersion: Either[Error, ShhVersion] = {
    val rq = GenericRequest(method = "shh_version")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(ShhVersion(rs.jsonrpc, rs.id, BigDecimal(rs.result.get.asInstanceOf[String])))
    }
  }
  override def shhInfo: Either[Error, ShhInfo] = {
    val rq = GenericRequest(method = "shh_info")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None =>
        rs.result.get match {
          case m: Map[_, _] =>
            import org.web3scala.json.JacksonReaders._
            val infoDetails = Extraction.decompose(m).as[ShhInfoDetails]
            Right(ShhInfo(rs.jsonrpc, rs.id, infoDetails))
        }
    }
  }
  override def shhNewSymKey: Either[Error, ShhNewSymKey] = {
    val rq = GenericRequest(method = "shh_newSymKey")
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(ShhNewSymKey(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def shhNewMessageFilter(obj: ShhNewMessageFilterObject): Either[Error, ShhMessageFilter] = {
    val rq = GenericRequest(method = "shh_newMessageFilter", params = obj :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(ShhMessageFilter(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[String]))
    }
  }
  override def shhPost(obj: ShhMessageObject): Either[Error, ShhPost] = {
    val rq = GenericRequest(method = "shh_post", params = obj :: Nil)
    val rs = executeSync(rq)
    rs.error match {
      case Some(e) => Left(Error(rs.jsonrpc, rs.id, e))
      case None => Right(ShhPost(rs.jsonrpc, rs.id, rs.result.get.asInstanceOf[Boolean]))
    }
  }






  private def processGetFilterLogsResponse(rs: GenericResponse): EthFilterLogs = {
    val result = rs.result.get.asInstanceOf[List[_]]
    if (result.isEmpty) {
      EthFilterLogs(rs.jsonrpc, rs.id, result)
    } else {
      result.head match {
        case m: HashMap[_, _] =>
          import org.web3scala.json.JacksonReaders._
          val logs = Extraction.decompose(rs.result.get).as[FilterLogs]
          EthFilterLogs(rs.jsonrpc, rs.id, logs.logs)
        case s: String =>
          EthFilterLogs(rs.jsonrpc, rs.id, result)
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
  override def asyncEthSendTransaction(obj: EthSendTransactionObject): AsyncResponse = {
    val rq = GenericRequest(method = "eth_sendTransaction", params = obj :: Nil)
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
  override def asyncEthEstimateGas(obj: EthEstimateGasObject): AsyncResponse = {
    val rq = GenericRequest(method = "eth_estimateGas", params = obj :: Nil)
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
  override def asyncEthNewFilter(obj: EthNewFilterObject): AsyncResponse = {
    val rq = GenericRequest(method = "eth_newFilter", params = obj :: Nil)
    executeAsync(rq)
  }
  override def asyncEthNewBlockFilter: AsyncResponse = {
    val rq = GenericRequest(method = "eth_newBlockFilter")
    executeAsync(rq)
  }
  override def asyncEthNewPendingTransactionFilter: AsyncResponse = {
    val rq = GenericRequest(method = "eth_newPendingTransactionFilter")
    executeAsync(rq)
  }
  override def asyncEthUninstallFilter(id: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_uninstallFilter", params = id :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetFilterChanges(id: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getFilterChanges", params = id :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetFilterLogs(id: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getFilterLogs", params = id :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetLogs(obj: EthNewFilterObject): AsyncResponse = {
    val rq = GenericRequest(method = "eth_getLogs", params = obj :: Nil)
    executeAsync(rq)
  }
  override def asyncEthGetWork: AsyncResponse = {
    val rq = GenericRequest(method = "eth_getWork")
    executeAsync(rq)
  }
  override def asyncEthSubmitWork(nonce: String, powHash: String, mixDigest: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_submitWork", params = nonce :: powHash :: mixDigest :: Nil)
    executeAsync(rq)
  }
  override def asyncEthSubmitHashrate(hashrate: String, clientId: String): AsyncResponse = {
    val rq = GenericRequest(method = "eth_submitHashrate", params = hashrate :: clientId :: Nil)
    executeAsync(rq)
  }
  override def asyncShhVersion: AsyncResponse = {
    val rq = GenericRequest(method = "shh_version")
    executeAsync(rq)
  }
  override def asyncShhInfo: AsyncResponse = {
    val rq = GenericRequest(method = "shh_info")
    executeAsync(rq)
  }
  override def asyncShhNewSymKey: AsyncResponse = {
    val rq = GenericRequest(method = "shh_newSymKey")
    executeAsync(rq)
  }
  override def asyncShhNewMessageFilter(obj: ShhNewMessageFilterObject): AsyncResponse = {
    val rq = GenericRequest(method = "shh_newMessageFilter", params = obj :: Nil)
    executeAsync(rq)
  }
  override def asyncShhPost(obj: ShhMessageObject): AsyncResponse = {
    val rq = GenericRequest(method = "shh_post", params = obj :: Nil)
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