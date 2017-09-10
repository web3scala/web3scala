package org.web3scala

import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.json4s.{DefaultFormats, Extraction}
import org.web3scala.http.{DispatchHttpClient, JValueHttpClient}
import org.web3scala.json.{JacksonJsonMapper, JsonMapper}
import org.web3scala.model._
import org.web3scala.util.Utils

import scala.collection.immutable.HashMap

class ServiceSpec extends FlatSpec with BeforeAndAfter with Matchers with MockitoSugar {

  var httpClientMock: JValueHttpClient = _
  var jsonMapperMock: JsonMapper = _

  before {
    httpClientMock = mock[DispatchHttpClient]
    jsonMapperMock = mock[JacksonJsonMapper]
  }

  private def service(rq: Request, rs: GenericResponse) = {

    implicit val formats: DefaultFormats.type = DefaultFormats

    val byteRequest = jsonMapperMock.writeAsBytes(rq)
    val jsonResponse = Extraction.decompose(rs)

    when(httpClientMock.sync(byteRequest)).thenReturn(jsonResponse)

    new Service(jsonMapperMock, httpClientMock)
  }

  "Service" should "return current Ethereum client version, when invoking web3ClientVersion method" in {

    val rq = Request(method = "web3_clientVersion")
    val rs = GenericResponse("2.0", 33, None, "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3")

    val response = service(rq, rs).web3ClientVersion

    response.asInstanceOf[Web3ClientVersion].result shouldBe "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3"
  }
  it should "return Keccak-256 of the given data, when invoking web3Sha3 method with valid input" in {

    val rqData = Utils.int2hex(123)
    val rq = Request(method = "web3_sha3", params = rqData :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0xa91eddf639b0b768929589c1a9fd21dcb0107199bdd82e55c5348018a1572f52")

    val response = service(rq, rs).web3Sha3(rqData)

    response.asInstanceOf[Web3Sha3].result shouldBe
      "0xa91eddf639b0b768929589c1a9fd21dcb0107199bdd82e55c5348018a1572f52"
  }
  it should "return Error object, when invoking web3Sha3 method with invalid input" in {

    val rqData = "test"
    val rq = Request(method = "web3_sha3", params = rqData :: Nil)
    val rs = GenericResponse("2.0", 33,
      Some(
        ErrorContent(-32602,
          "invalid argument 0: json: cannot unmarshal hex string without 0x prefix into Go value of type hexutil.Bytes"
        )
      ), AnyRef
    )

    val response = service(rq, rs).web3Sha3(rqData)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32602
    response.asInstanceOf[Error].error.message shouldBe
      "invalid argument 0: json: cannot unmarshal hex string without 0x prefix into Go value of type hexutil.Bytes"
  }
  it should "return current network id, when invoking netVersion method" in {

    val rq = Request(method = "net_version")
    val rs = GenericResponse("2.0", 33, None, "3")

    val response = service(rq, rs).netVersion

    response.asInstanceOf[NetVersion].result shouldBe 3
  }
  it should "return true if client is actively listening for network connections, when invoking netListening method" in {

    val rq = Request(method = "net_listening")
    val rs = GenericResponse("2.0", 33, None, true)

    val response = service(rq, rs).netListening

    response.asInstanceOf[NetListening].result shouldBe true
  }
  it should "return number of peers currently connected to the client, when invoking netPeerCount method" in {

    val rq = Request(method = "net_peerCount")
    val rs = GenericResponse("2.0", 33, None, "0xB")

    val response = service(rq, rs).netPeerCount

    response.asInstanceOf[NetPeerCount].result shouldBe 11
  }
  it should "return the current ethereum protocol version, when invoking ethProtocolVersion method" in {

    val rq = Request(method = "eth_protocolVersion")
    val rs = GenericResponse("2.0", 33, None, "0x3F")

    val response = service(rq, rs).ethProtocolVersion

    response.asInstanceOf[EthProtocolVersion].result shouldBe 63
  }
  it should "return false, when invoking ethSyncing method and synchronization with blockchain is not taking place" in {

    val rq = Request(method = "eth_syncing")
    val rs = GenericResponse("2.0", 33, None, false)

    val response = service(rq, rs).ethSyncing

    response.asInstanceOf[EthSyncingFalse].result shouldBe false
  }
  it should "return an object with data about the sync status, when invoking ethSyncing method and synchronization with blockchain is taking place" in {

    val rsData = HashMap(
      ("pulledStates", "0x1307"),
      ("knownStates", "0x2527"),
      ("currentBlock", "0x1d640"),
      ("highestBlock", "0xcbccb"),
      ("startingBlock", "0x1d640")
    )
    val rq = Request(method = "eth_syncing")
    val rs = GenericResponse("2.0", 33, None, rsData)

    val response = service(rq, rs).ethSyncing

    response.asInstanceOf[EthSyncingTrue].result.mkString(", ") shouldBe
      "pulledStates -> 4871, knownStates -> 9511, currentBlock -> 120384, highestBlock -> 834763, startingBlock -> 120384"
  }
  it should "return the client coinbase address, when invoking ethCoinbase method" in {

    val rq = Request(method = "eth_coinbase")
    val rs = GenericResponse("2.0", 33, None, "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617")

    val response = service(rq, rs).ethCoinbase

    response.asInstanceOf[EthCoinbase].result shouldBe "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
  }
  it should "return true if client is actively mining new blocks, when invoking ethMining method" in {

    val rq = Request(method = "eth_mining")
    val rs = GenericResponse("2.0", 33, None, false)

    val response = service(rq, rs).ethMining

    response.asInstanceOf[EthMining].result shouldBe false
  }
  it should "return the number of hashes per second that the node is mining with, when invoking ethHashrate method" in {

    val rq = Request(method = "eth_hashrate")
    val rs = GenericResponse("2.0", 33, None, "0x0")

    val response = service(rq, rs).ethHashrate

    response.asInstanceOf[EthHashrate].result shouldBe 0
  }
  it should "return the current price per gas in wei, when invoking ethGasPrice method" in {

    val rq = Request(method = "eth_gasPrice")
    val rs = GenericResponse("2.0", 33, None, "0x6FC23AC00")

    val response = service(rq, rs).ethGasPrice

    response.asInstanceOf[EthGasPrice].result shouldBe 30000000000L
  }
  it should "return list of addresses owned by client, when invoking ethAccounts method" in {

    val rsData = List(
      "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617",
      "0xd179a76b1d0a91dc8287afc9032cae34f283873d",
      "0xf9c510e90bcb47cc49549e57b80814ae3a8bb683"
    )
    val rq = Request(method = "eth_accounts")
    val rs = GenericResponse("2.0", 33, None, rsData)

    val response = service(rq, rs).ethAccounts

    response.asInstanceOf[EthAccounts].result.mkString(", ") shouldBe
      "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617, 0xd179a76b1d0a91dc8287afc9032cae34f283873d, 0xf9c510e90bcb47cc49549e57b80814ae3a8bb683"
  }
  it should "return the number of most recent block, when invoking ethBlockNumber method" in {

    val rq = Request(method = "eth_blockNumber")
    val rs = GenericResponse("2.0", 33, None, "0x18AA03")

    val response = service(rq, rs).ethBlockNumber

    response.asInstanceOf[EthBlockNumber].result shouldBe 1616387L
  }
  it should "return the balance of the account of given address, when invoking ethGetBalance method with block number" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val blockNumber = BlockNumber(1559297)
    val block = Service.blockValue(blockNumber)

    val rq = Request(method = "eth_getBalance", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0x491C86A7F255B000")

    val response = service(rq, rs).ethGetBalance(address, blockNumber)

    response.asInstanceOf[EthBalance].result shouldBe 5268233720000000000L
  }
  it should "return Error object, when invoking ethGetBalance method with invalid input" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val blockNumber = BlockNumber(156898)
    val block = Service.blockValue(blockNumber)

    val rq = Request(method = "eth_getBalance", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33,
      Some(
        ErrorContent(-32000,
          "missing trie node 81f524d8384c88d5104a749895c8ed6d3f1a01c8c5f78bd547f74c10862964bc (path )"
        )
      ), AnyRef
    )

    val response = service(rq, rs).ethGetBalance(address, blockNumber)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32000
    response.asInstanceOf[Error].error.message shouldBe
      "missing trie node 81f524d8384c88d5104a749895c8ed6d3f1a01c8c5f78bd547f74c10862964bc (path )"
  }
  it should "return the balance of the account of given address, when invoking ethGetBalance method with block name" in {

    val address = "0xf9C510e90bCb47cc49549e57b80814aE3A8bb683"
    val blockName = BlockName("latest")
    val block = Service.blockValue(blockName)

    val rq = Request(method = "eth_getBalance", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0x6F05B59D3B20000")

    val response = service(rq, rs).ethGetBalance(address, blockName)

    response.asInstanceOf[EthBalance].result shouldBe 500000000000000000L
  }
  it should "return the value from a storage position at a given address, when invoking ethGetStorageAt method" in {

    val address = "0x902c4fD71e196E86e7C82126Ff88ADa63a590d22"
    val position = Utils.int2hex(1)
    val blockName = BlockName("latest")
    val block = Service.blockValue(blockName)

    val rq = Request(method = "eth_getStorageAt", params = address :: position :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0x0000000000000000000000000000000000000000000000000000000000000000")

    val response = service(rq, rs).ethGetStorageAt(address, position, blockName)

    response.asInstanceOf[EthStorage].result shouldBe "0x0000000000000000000000000000000000000000000000000000000000000000"
  }
  it should "return the value, for an element of the map, from a storage position at a given address, when invoking ethGetStorageAt method" is pending
  it should "return the number of transactions sent from an address, when invoking ethGetTransactionCount method" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val blockName = BlockName("latest")
    val block = Service.blockValue(blockName)

    val rq = Request(method = "eth_getTransactionCount", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0xA")

    val response = service(rq, rs).ethGetTransactionCount(address, blockName)

    response.asInstanceOf[EthTransactionCount].result shouldBe 10
  }
  it should "return the number of transactions in a block from a block matching the given block hash, when invoking ethGetBlockTransactionCountByHash method" in {

    val blockHash = "0xc40da02dbc5bb5cbde7c8c8cb7923797afc3078e3589b5537ec72b4726da8843"

    val rq = Request(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0x8")

    val response = service(rq, rs).ethGetBlockTransactionCountByHash(blockHash)

    response.asInstanceOf[EthBlockTransactionCount].result shouldBe 8
  }
  it should "return Error object, when invoking ethGetBlockTransactionCountByHash method with invalid input" in {

    val blockHash = "0x9b2055d370f73ec7d8a03e965129118dc8f5bf83"

    val rq = Request(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    val rs = GenericResponse("2.0", 33,
      Some(
        ErrorContent(-32602,
          "invalid argument 0: hex string has length 40, want 64 for common.Hash"
        )
      ), AnyRef
    )

    val response = service(rq, rs).ethGetBlockTransactionCountByHash(blockHash)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32602
    response.asInstanceOf[Error].error.message shouldBe
      "invalid argument 0: hex string has length 40, want 64 for common.Hash"
  }
  it should "return the number of transactions in a block from a block matching the given block number, when invoking ethGetBlockTransactionCountByNumber method" in {

    val blockNumber = BlockNumber(1128977)
    val block = Service.blockValue(blockNumber)

    val rq = Request(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0x6")

    val response = service(rq, rs).ethGetBlockTransactionCountByNumber(blockNumber)

    response.asInstanceOf[EthBlockTransactionCount].result shouldBe 6
  }
  it should "return the number of uncles in a block from a block matching the given block hash, when invoking ethGetUncleCountByBlockHash method" in {

    val blockHash = "0x7c70252114eafc143743e998eb5dbf11b2c61a716590982821fdd13f174ed891"

    val rq = Request(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0x0")

    val response = service(rq, rs).ethGetUncleCountByBlockHash(blockHash)

    response.asInstanceOf[EthUncleCount].result shouldBe 0
  }
  it should "return the number of uncles in a block from a block matching the given block number, when invoking ethGetUncleCountByBlockNumber method" in {

    val blockNumber = BlockNumber(1128977)
    val block = Service.blockValue(blockNumber)

    val rq = Request(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0x0")

    val response = service(rq, rs).ethGetUncleCountByBlockNumber(blockNumber)

    response.asInstanceOf[EthUncleCount].result shouldBe 0
  }
  it should "return code at a given address, when invoking ethGetCode method" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val blockNumber = BlockNumber(1128977)
    val block = Service.blockValue(blockNumber)

    val rq = Request(method = "eth_getCode", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0x0")

    val response = service(rq, rs).ethGetCode(address, blockNumber)

    response.asInstanceOf[EthCode].result shouldBe "0x0"
  }
  it should "return calculated Ethereum specific signature, when invoking ethSign method" in {

    val address = "0xf9C510e90bCb47cc49549e57b80814aE3A8bb683"
    val message = "0xdeadbeef"

    val rq = Request(method = "eth_sign", params = address :: message :: Nil)
    val rs = GenericResponse("2.0", 33, None,
      "0xa3f20717a250c2b0b729b7e5becbff67fdaef7e0699da4de7ca5895b02a170a12d887fd3b17bfdce3481f10bea41f45ba9f709d39ce8325427b57afcfc994cee1b"
    )

    val response = service(rq, rs).ethSign(address, message)

    response.asInstanceOf[EthSign].result shouldBe
      "0xa3f20717a250c2b0b729b7e5becbff67fdaef7e0699da4de7ca5895b02a170a12d887fd3b17bfdce3481f10bea41f45ba9f709d39ce8325427b57afcfc994cee1b"
  }
  it should "return Error object, when invoking ethSign method with unknown account" in {

    val address = "0x9b2055d370f73ec7d8a03e965129118dc8f5bf83"
    val message = "0xdeadbeef"

    val rq = Request(method = "eth_sign", params = address :: message :: Nil)
    val rs = GenericResponse("2.0", 33, Some(ErrorContent(-32000, "unknown account")), AnyRef)

    val response = service(rq, rs).ethSign(address, message)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32000
    response.asInstanceOf[Error].error.message shouldBe "unknown account"
  }
  it should "return Error object, when invoking ethSign method with locked account" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val message = "0xdeadbeef"

    val rq = Request(method = "eth_sign", params = address :: message :: Nil)
    val rs = GenericResponse("2.0", 33, Some(ErrorContent(-32000, "authentication needed: password or unlock")), AnyRef)

    val response = service(rq, rs).ethSign(address, message)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32000
    response.asInstanceOf[Error].error.message shouldBe "authentication needed: password or unlock"
  }
  it should "create new message call transaction or a create a contract, if the data field contains code, when invoking ethSendTransaction method" in {

    val params = HashMap(
      "from" -> "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617",
      "to" -> "0xd179a76b1d0a91dc8287afc9032cae34f283873d",
      "gas" -> "0x76c0",
      "gasPrice" -> "0x9184e72a000",
      "value" -> "0x9184e72a",
      "data" -> "0x68656c6c6f",
      "nonce" -> ""
    )
    val rq = Request(method = "eth_sendTransaction", params = params)
    val rs = GenericResponse("2.0", 33, None, "0x88146924ed5462e0c213b2c1f7d2c4a9f8a3218218a27642b5ea632e465b5a42")

    val response = service(rq, rs).ethSendTransaction(
      params("from"),
      Some(params("to")),
      Some(params("gas")),
      Some(params("gasPrice")),
      Some(params("value")),
      params("data"),
      Some(params("nonce"))
    )

    response.asInstanceOf[EthTransaction].result shouldBe "0x88146924ed5462e0c213b2c1f7d2c4a9f8a3218218a27642b5ea632e465b5a42"
  }
  it should "return Error object, when invoking ethSendTransaction method with locked account" in {

    val params = HashMap(
      "from" -> "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617",
      "to" -> "0xd179a76b1d0a91dc8287afc9032cae34f283873d",
      "gas" -> "0x76c0",
      "gasPrice" -> "0x9184e72a000",
      "value" -> "0x9184e72a",
      "data" -> "0x68656c6c6f",
      "nonce" -> ""
    )
    val rq = Request(method = "eth_sendTransaction", params = params)
    val rs = GenericResponse("2.0", 33, Some(ErrorContent(-32000, "authentication needed: password or unlock")), AnyRef)

    val response = service(rq, rs).ethSendTransaction(
      params("from"),
      Some(params("to")),
      Some(params("gas")),
      Some(params("gasPrice")),
      Some(params("value")),
      params("data"),
      Some(params("nonce"))
    )

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32000
    response.asInstanceOf[Error].error.message shouldBe "authentication needed: password or unlock"
  }
  it should "create new message call transaction or a contract creation for signed transactions, when invoking ethSendRawTransaction method" in {

    val data = "0xf9C510e90bCb47cc49549e57b80814aE3A8bb683"

    val rq = Request(method = "eth_sendRawTransaction", params = data :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331")

    val response = service(rq, rs).ethSendRawTransaction(data)

    response.asInstanceOf[EthTransaction].result shouldBe "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331"
  }
  it should "return Error object, when invoking ethSendRawTransaction method with element larger than containing list" in {

    val data = "0xf9C510e90bCb47cc49549e57b80814aE3A8bb683"

    val rq = Request(method = "eth_sendRawTransaction", params = data :: Nil)
    val rs = GenericResponse("2.0", 33, Some(ErrorContent(-32000, "rlp: element is larger than containing list")), AnyRef)

    val response = service(rq, rs).ethSendRawTransaction(data)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32000
    response.asInstanceOf[Error].error.message shouldBe "rlp: element is larger than containing list"
  }


}
