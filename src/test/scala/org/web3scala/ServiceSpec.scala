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

  private def service(rq: GenericRequest, rs: GenericResponse) = {

    implicit val formats: DefaultFormats.type = DefaultFormats

    val byteRequest = jsonMapperMock.writeAsBytes(rq)
    val jsonResponse = Extraction.decompose(rs)

    when(httpClientMock.sync(byteRequest)).thenReturn(jsonResponse)

    new Service(jsonMapperMock, httpClientMock)
  }

  "Service" should "return current Ethereum client version, when invoking web3ClientVersion method" in {

    val rq = GenericRequest(method = "web3_clientVersion")
    val rs = GenericResponse("2.0", 33, None, Some("Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3"))

    val response = service(rq, rs).web3ClientVersion

    response.asInstanceOf[Web3ClientVersion].result shouldBe "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3"
  }
  it should "return Keccak-256 of the given data, when invoking web3Sha3 method with valid input" in {

    val rqData = Utils.int2hex(123)
    val rq = GenericRequest(method = "web3_sha3", params = rqData :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0xa91eddf639b0b768929589c1a9fd21dcb0107199bdd82e55c5348018a1572f52"))

    val response = service(rq, rs).web3Sha3(rqData)

    response.asInstanceOf[Web3Sha3].result shouldBe "0xa91eddf639b0b768929589c1a9fd21dcb0107199bdd82e55c5348018a1572f52"
  }
  it should "return Error object, when invoking web3Sha3 method with invalid input" in {

    val rqData = "test"
    val rq = GenericRequest(method = "web3_sha3", params = rqData :: Nil)
    val rs = GenericResponse("2.0", 33,
      Some(
        ErrorContent(-32602,
          "invalid argument 0: json: cannot unmarshal hex string without 0x prefix into Go value of type hexutil.Bytes"
        )
      ), Some(AnyRef)
    )

    val response = service(rq, rs).web3Sha3(rqData)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32602
    response.asInstanceOf[Error].error.message shouldBe
      "invalid argument 0: json: cannot unmarshal hex string without 0x prefix into Go value of type hexutil.Bytes"
  }
  it should "return current network id, when invoking netVersion method" in {

    val rq = GenericRequest(method = "net_version")
    val rs = GenericResponse("2.0", 33, None, Some("3"))

    val response = service(rq, rs).netVersion

    response.asInstanceOf[NetVersion].result shouldBe 3
  }
  it should "return true if client is actively listening for network connections, when invoking netListening method" in {

    val rq = GenericRequest(method = "net_listening")
    val rs = GenericResponse("2.0", 33, None, Some(true))

    val response = service(rq, rs).netListening

    response.asInstanceOf[NetListening].result shouldBe true
  }
  it should "return number of peers currently connected to the client, when invoking netPeerCount method" in {

    val rq = GenericRequest(method = "net_peerCount")
    val rs = GenericResponse("2.0", 33, None, Some("0xB"))

    val response = service(rq, rs).netPeerCount

    response.asInstanceOf[NetPeerCount].result shouldBe 11
  }
  it should "return the current ethereum protocol version, when invoking ethProtocolVersion method" in {

    val rq = GenericRequest(method = "eth_protocolVersion")
    val rs = GenericResponse("2.0", 33, None, Some("0x3F"))

    val response = service(rq, rs).ethProtocolVersion

    response.asInstanceOf[EthProtocolVersion].result shouldBe 63
  }
  it should "return false, when invoking ethSyncing method and synchronization with blockchain is not taking place" in {

    val rq = GenericRequest(method = "eth_syncing")
    val rs = GenericResponse("2.0", 33, None, Some(false))

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
    val rq = GenericRequest(method = "eth_syncing")
    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethSyncing

    response.asInstanceOf[EthSyncingTrue].result.mkString(", ") shouldBe
      "pulledStates -> 4871, knownStates -> 9511, currentBlock -> 120384, highestBlock -> 834763, startingBlock -> 120384"
  }
  it should "return the client coinbase address, when invoking ethCoinbase method" in {

    val rq = GenericRequest(method = "eth_coinbase")
    val rs = GenericResponse("2.0", 33, None, Some("0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"))

    val response = service(rq, rs).ethCoinbase

    response.asInstanceOf[EthCoinbase].result shouldBe "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
  }
  it should "return true if client is actively mining new blocks, when invoking ethMining method" in {

    val rq = GenericRequest(method = "eth_mining")
    val rs = GenericResponse("2.0", 33, None, Some(false))

    val response = service(rq, rs).ethMining

    response.asInstanceOf[EthMining].result shouldBe false
  }
  it should "return the number of hashes per second that the node is mining with, when invoking ethHashrate method" in {

    val rq = GenericRequest(method = "eth_hashrate")
    val rs = GenericResponse("2.0", 33, None, Some("0x0"))

    val response = service(rq, rs).ethHashrate

    response.asInstanceOf[EthHashrate].result shouldBe 0
  }
  it should "return the current price per gas in wei, when invoking ethGasPrice method" in {

    val rq = GenericRequest(method = "eth_gasPrice")
    val rs = GenericResponse("2.0", 33, None, Some("0x6FC23AC00"))

    val response = service(rq, rs).ethGasPrice

    response.asInstanceOf[EthGasPrice].result shouldBe 30000000000L
  }
  it should "return list of addresses owned by client, when invoking ethAccounts method" in {

    val rsData = List(
      "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617",
      "0xd179a76b1d0a91dc8287afc9032cae34f283873d",
      "0xf9c510e90bcb47cc49549e57b80814ae3a8bb683"
    )
    val rq = GenericRequest(method = "eth_accounts")
    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethAccounts

    response.asInstanceOf[EthAccounts].result.mkString(", ") shouldBe
      "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617, 0xd179a76b1d0a91dc8287afc9032cae34f283873d, 0xf9c510e90bcb47cc49549e57b80814ae3a8bb683"
  }
  it should "return the number of most recent block, when invoking ethBlockNumber method" in {

    val rq = GenericRequest(method = "eth_blockNumber")
    val rs = GenericResponse("2.0", 33, None, Some("0x18AA03"))

    val response = service(rq, rs).ethBlockNumber

    response.asInstanceOf[EthBlockNumber].result shouldBe 1616387L
  }
  it should "return the balance of the account of given address, when invoking ethGetBalance method with block number" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val blockNumber = BlockNumber(1559297)
    val block = Service.blockValue(blockNumber)

    val rq = GenericRequest(method = "eth_getBalance", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x491C86A7F255B000"))

    val response = service(rq, rs).ethGetBalance(address, blockNumber)

    response.asInstanceOf[EthBalance].result shouldBe 5268233720000000000L
  }
  it should "return Error object, when invoking ethGetBalance method with invalid input" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val blockNumber = BlockNumber(156898)
    val block = Service.blockValue(blockNumber)

    val rq = GenericRequest(method = "eth_getBalance", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33,
      Some(
        ErrorContent(-32000,
          "missing trie node 81f524d8384c88d5104a749895c8ed6d3f1a01c8c5f78bd547f74c10862964bc (path )"
        )
      ), Some(AnyRef)
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

    val rq = GenericRequest(method = "eth_getBalance", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x6F05B59D3B20000"))

    val response = service(rq, rs).ethGetBalance(address, blockName)

    response.asInstanceOf[EthBalance].result shouldBe 500000000000000000L
  }
  it should "return the value from a storage position at a given address, when invoking ethGetStorageAt method" in {

    val address = "0x902c4fD71e196E86e7C82126Ff88ADa63a590d22"
    val position = Utils.int2hex(1)
    val blockName = BlockName("latest")
    val block = Service.blockValue(blockName)

    val rq = GenericRequest(method = "eth_getStorageAt", params = address :: position :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x0000000000000000000000000000000000000000000000000000000000000000"))

    val response = service(rq, rs).ethGetStorageAt(address, position, blockName)

    response.asInstanceOf[EthStorage].result shouldBe "0x0000000000000000000000000000000000000000000000000000000000000000"
  }
  it should "return the value, for an element of the map, from a storage position at a given address, when invoking ethGetStorageAt method" is pending
  it should "return the number of transactions sent from an address, when invoking ethGetTransactionCount method" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val blockName = BlockName("latest")
    val block = Service.blockValue(blockName)

    val rq = GenericRequest(method = "eth_getTransactionCount", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0xA"))

    val response = service(rq, rs).ethGetTransactionCount(address, blockName)

    response.asInstanceOf[EthTransactionCount].result shouldBe 10
  }
  it should "return the number of transactions in a block from a block matching the given block hash, when invoking ethGetBlockTransactionCountByHash method" in {

    val blockHash = "0xc40da02dbc5bb5cbde7c8c8cb7923797afc3078e3589b5537ec72b4726da8843"

    val rq = GenericRequest(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x8"))

    val response = service(rq, rs).ethGetBlockTransactionCountByHash(blockHash)

    response.asInstanceOf[EthBlockTransactionCount].result shouldBe 8
  }
  it should "return Error object, when invoking ethGetBlockTransactionCountByHash method with invalid input" in {

    val blockHash = "0x9b2055d370f73ec7d8a03e965129118dc8f5bf83"

    val rq = GenericRequest(method = "eth_getBlockTransactionCountByHash", params = blockHash :: Nil)
    val rs = GenericResponse("2.0", 33,
      Some(
        ErrorContent(-32602,
          "invalid argument 0: hex string has length 40, want 64 for common.Hash"
        )
      ), Some(AnyRef)
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

    val rq = GenericRequest(method = "eth_getBlockTransactionCountByNumber", params = block :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x6"))

    val response = service(rq, rs).ethGetBlockTransactionCountByNumber(blockNumber)

    response.asInstanceOf[EthBlockTransactionCount].result shouldBe 6
  }
  it should "return the number of uncles in a block from a block matching the given block hash, when invoking ethGetUncleCountByBlockHash method" in {

    val blockHash = "0x7c70252114eafc143743e998eb5dbf11b2c61a716590982821fdd13f174ed891"

    val rq = GenericRequest(method = "eth_getUncleCountByBlockHash", params = blockHash :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x0"))

    val response = service(rq, rs).ethGetUncleCountByBlockHash(blockHash)

    response.asInstanceOf[EthUncleCount].result shouldBe 0
  }
  it should "return the number of uncles in a block from a block matching the given block number, when invoking ethGetUncleCountByBlockNumber method" in {

    val blockNumber = BlockNumber(1128977)
    val block = Service.blockValue(blockNumber)

    val rq = GenericRequest(method = "eth_getUncleCountByBlockNumber", params = block :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x0"))

    val response = service(rq, rs).ethGetUncleCountByBlockNumber(blockNumber)

    response.asInstanceOf[EthUncleCount].result shouldBe 0
  }
  it should "return code at a given address, when invoking ethGetCode method" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val blockNumber = BlockNumber(1128977)
    val block = Service.blockValue(blockNumber)

    val rq = GenericRequest(method = "eth_getCode", params = address :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x0"))

    val response = service(rq, rs).ethGetCode(address, blockNumber)

    response.asInstanceOf[EthCode].result shouldBe "0x0"
  }
  it should "return calculated Ethereum specific signature, when invoking ethSign method" in {

    val address = "0xf9C510e90bCb47cc49549e57b80814aE3A8bb683"
    val message = "0xdeadbeef"

    val rq = GenericRequest(method = "eth_sign", params = address :: message :: Nil)
    val rs = GenericResponse("2.0", 33, None,
      Some("0xa3f20717a250c2b0b729b7e5becbff67fdaef7e0699da4de7ca5895b02a170a12d887fd3b17bfdce3481f10bea41f45ba9f709d39ce8325427b57afcfc994cee1b")
    )

    val response = service(rq, rs).ethSign(address, message)

    response.asInstanceOf[EthSign].result shouldBe
      "0xa3f20717a250c2b0b729b7e5becbff67fdaef7e0699da4de7ca5895b02a170a12d887fd3b17bfdce3481f10bea41f45ba9f709d39ce8325427b57afcfc994cee1b"
  }
  it should "return Error object, when invoking ethSign method with unknown account" in {

    val address = "0x9b2055d370f73ec7d8a03e965129118dc8f5bf83"
    val message = "0xdeadbeef"

    val rq = GenericRequest(method = "eth_sign", params = address :: message :: Nil)
    val rs = GenericResponse("2.0", 33, Some(ErrorContent(-32000, "unknown account")), Some(AnyRef))

    val response = service(rq, rs).ethSign(address, message)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32000
    response.asInstanceOf[Error].error.message shouldBe "unknown account"
  }
  it should "return Error object, when invoking ethSign method with locked account" in {

    val address = "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
    val message = "0xdeadbeef"

    val rq = GenericRequest(method = "eth_sign", params = address :: message :: Nil)
    val rs = GenericResponse("2.0", 33, Some(ErrorContent(-32000, "authentication needed: password or unlock")), Some(AnyRef))

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
    val rq = GenericRequest(method = "eth_sendTransaction", params = params)
    val rs = GenericResponse("2.0", 33, None, Some("0x88146924ed5462e0c213b2c1f7d2c4a9f8a3218218a27642b5ea632e465b5a42"))

    val response = service(rq, rs).ethSendTransaction(
      params("from"),
      Some(params("to")),
      Some(params("gas")),
      Some(params("gasPrice")),
      Some(params("value")),
      params("data"),
      Some(params("nonce"))
    )

    response.asInstanceOf[EthSendTransaction].result shouldBe "0x88146924ed5462e0c213b2c1f7d2c4a9f8a3218218a27642b5ea632e465b5a42"
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
    val rq = GenericRequest(method = "eth_sendTransaction", params = params)
    val rs = GenericResponse("2.0", 33, Some(ErrorContent(-32000, "authentication needed: password or unlock")), Some(AnyRef))

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

    val signedTransactionData = "0xf9C510e90bCb47cc49549e57b80814aE3A8bb683"

    val rq = GenericRequest(method = "eth_sendRawTransaction", params = signedTransactionData :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331"))

    val response = service(rq, rs).ethSendRawTransaction(signedTransactionData)

    response.asInstanceOf[EthSendTransaction].result shouldBe "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331"
  }
  it should "return Error object, when invoking ethSendRawTransaction method with element larger than containing list" in {

    val signedTransactionData = "0xf9C510e90bCb47cc49549e57b80814aE3A8bb683"

    val rq = GenericRequest(method = "eth_sendRawTransaction", params = signedTransactionData :: Nil)
    val rs = GenericResponse("2.0", 33, Some(ErrorContent(-32000, "rlp: element is larger than containing list")), Some(AnyRef))

    val response = service(rq, rs).ethSendRawTransaction(signedTransactionData)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32000
    response.asInstanceOf[Error].error.message shouldBe "rlp: element is larger than containing list"
  }
  it should "execute a new message call immediately without creating a transaction on the block chain, when invoking ethCall method" in {

    val ethCallObj = EthCallObject(
      Some("0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"),
      "0xd179a76b1d0a91dc8287afc9032cae34f283873d",
      Some("0x76c0"),
      Some("0x9184e72a000"),
      Some("0x9184e72a"),
      Some("0x68656c6c6f")
    )
    val blockNumber  = BlockNumber(1128977)

    val block = Service.blockValue(blockNumber)

    val rq = GenericRequest(method = "eth_call", params = ethCallObj :: block :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x"))

    val response = service(rq, rs).ethCall(ethCallObj, blockNumber)

    response.asInstanceOf[EthCall].result shouldBe "0x"
  }
  it should "make a call or transaction, which won't be added to the blockchain and return the used gas, which " +
    "can be used for estimating the used gas, when invoking ethEstimateGas method" in {

    val params = HashMap(
      "from" -> "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617",
      "to" -> "0xd179a76b1d0a91dc8287afc9032cae34f283873d",
      "gas" -> "0x76c0",
      "gasPrice" -> "0x9184e72a000",
      "value" -> "0x9184e72a",
      "data" -> "0x68656c6c6f"
    )
    val rq = GenericRequest(method = "eth_call", params = params :: Nil)
    val rs = GenericResponse("2.0", 33, None, Some("0x535D"))

    val response = service(rq, rs).ethEstimateGas(
      Some(params("from")),
      params("to"),
      Some(params("gas")),
      Some(params("gasPrice")),
      Some(params("value")),
      Some(params("data"))
    )

    response.asInstanceOf[EthEstimatedGas].result shouldBe 21341
  }
  it should "return information about a block by hash, when invoking ethGetBlockByHash method" in {

    val blockHash = "0xacf2a4907cfbfc1b181928893c0375714fad20d4e2877b20822d55370d101c01"
    val fullTransactionObjects = true

    val rq = GenericRequest(method = "eth_getBlockByHash", params = blockHash :: fullTransactionObjects :: Nil)

    val rsData = HashMap(
      "number" -> "0x1919F7",
      "hash" -> "0xacf2a4907cfbfc1b181928893c0375714fad20d4e2877b20822d55370d101c01",
      "parentHash" -> "0xdb7697788ed0a25a883f3384592df414ae55602f9fa9dad4ae6c27f10f86e5b3",
      "mixHash" -> "0x3c5eef4d518b2ce65a37e2a7f6139335b4f4e0cefa01ef1bebf58106ee3ba338",
      "nonce" -> "0x4547a918a1c230a1",
      "transactionsRoot" -> "0x76aa6e6f9bc8963459ee3ff346553e2ac1ab1d91777d97d988b8cc174dc9b862",
      "stateRoot" -> "0xfcacbf5a391a32687f89511abc35b70c9419ec1aed21009f90a18f6f9301a6b7",
      "receiptsRoot" -> "0x823be7358d1284d6e12609cb364afb4bdab653f9ca510774a556aae232bf4d73",
      "sha3Uncles" -> "0xbfc0f819d3ed8cbf350e18f92a8a444cba78e9dd1c1073a83399237cf464f772",
      "logsBloom" -> "0x040022140042004000420000000000000005000000000000000000000000000008000100100040000060000000002000000040000000000000001000000000000000000000000000000c0000080100000020000000000000000040008050200000000008020440000000008000000c004400000000400400008000000000002000000088000000200020000000000000000000000800000000c0000000000088000000000008000000020100000000002000020000000080200000000404000012000000010000200000000000000080000000000000000000000000000020000000000002000010000200000000000008000000008000000000000000000000",
      "miner" -> "0xc56",
      "difficulty" -> "0x1035EDAA0",
      "totalDifficulty" -> "0x6364D76BE9D7B",
      "extraData" -> "0xd883010607846765746887676f312e382e338664617277696e",
      "size" -> "0xC56",
      "gasLimit" -> "0x47E7C4",
      "gasUsed" -> "0xBE611",
      "timestamp" -> "0x59B4A81A",
      "transactions" -> List(
        HashMap(
          "s" -> "0x418b08924f17a5ea30b8433f1cbb08eafc445706936df89135945040258b2ce3",
          "blockHash" -> "0xacf2a4907cfbfc1b181928893c0375714fad20d4e2877b20822d55370d101c01",
          "nonce" -> "0xD334",
          "gasPrice" -> "0x6C088E200",
          "gas" -> "0x5209",
          "to" -> "0x8c3704a8612e7303eacaa5fe135482cef0c52556",
          "v" -> "0x1C",
          "hash" -> "0xe16846a4c2a0de9b4fa99b756ac4528bc8706612929dc386a27b75d4e545711e",
          "from" -> "0x81b7e08f65bdf5648606c89998a9cc8164397647",
          "blockNumber" -> "0x1919F7",
          "r" -> "0xab1d1bbd289ba86a8d2debc76389bb8bf8f592b797dbac7b568f72dcd73cdc8",
          "value" -> "0xDE0B6B3A7640000",
          "input" -> "0x",
          "transactionIndex" -> "0x0"
        ),
        HashMap(
          "s" -> "0x407387c9649c4452a75f49428d32dec9b59f773247cc12305de4d9ae88e9fa98",
          "blockHash" -> "0xacf2a4907cfbfc1b181928893c0375714fad20d4e2877b20822d55370d101c01",
          "nonce" -> "0xD335",
          "gasPrice" -> "0x6C088E200",
          "gas" -> "0x5209",
          "to" -> "0x8c3704a8612e7303eacaa5fe135482cef0c52556",
          "v" -> "0x1B",
          "hash" -> "0x0c86abbe3f4d3d1ecc5d3ef7b1acce2c780dfc923d1d0c34f95de16679d1e679",
          "from" -> "0x81b7e08f65bdf5648606c89998a9cc8164397647",
          "blockNumber" -> "0x1919F7",
          "r" -> "0xf8480c80d131a1619fe03c9c5abd219aca57cac76821c49c30bb16de2c66e403",
          "value" -> "0xDE0B6B3A7640000",
          "input" -> "0x",
          "transactionIndex" -> "0x1"
        )
      ),
      "uncles" -> List(
        "0x7a77093b82b5dff8af33954b16b51a93543d88cba8c814ad29c29f38f09e49f7",
        "0x7a28ae12786ff6cac0eb55feda9897c85a14d1fd9ad9a7c41fd1071bbc162de9"
      )
    )

    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethGetBlockByHash(blockHash, fullTransactionObjects)

    val actualResult = response.asInstanceOf[EthBlock].result.get.asInstanceOf[BlockWithTransactions].nonce
    val expectedResult = Utils.hex2long(rsData("nonce").toString)

    actualResult shouldBe expectedResult
  }
  it should "return information about a block by block number, when invoking ethGetBlockByNumber method" in {

    val blockNumber  = BlockNumber(1128977)
    val block = Service.blockValue(blockNumber)
    val fullTransactionObjects = true

    val rq = GenericRequest(method = "eth_getBlockByNumber", params = block :: fullTransactionObjects :: Nil)

    val rsData = HashMap(
      "number" -> "0x1919F7",
      "hash" -> "0xacf2a4907cfbfc1b181928893c0375714fad20d4e2877b20822d55370d101c01",
      "parentHash" -> "0xdb7697788ed0a25a883f3384592df414ae55602f9fa9dad4ae6c27f10f86e5b3",
      "mixHash" -> "0x3c5eef4d518b2ce65a37e2a7f6139335b4f4e0cefa01ef1bebf58106ee3ba338",
      "nonce" -> "0x4547a918a1c230a1",
      "transactionsRoot" -> "0x76aa6e6f9bc8963459ee3ff346553e2ac1ab1d91777d97d988b8cc174dc9b862",
      "stateRoot" -> "0xfcacbf5a391a32687f89511abc35b70c9419ec1aed21009f90a18f6f9301a6b7",
      "receiptsRoot" -> "0x823be7358d1284d6e12609cb364afb4bdab653f9ca510774a556aae232bf4d73",
      "sha3Uncles" -> "0xbfc0f819d3ed8cbf350e18f92a8a444cba78e9dd1c1073a83399237cf464f772",
      "logsBloom" -> "0x040022140042004000420000000000000005000000000000000000000000000008000100100040000060000000002000000040000000000000001000000000000000000000000000000c0000080100000020000000000000000040008050200000000008020440000000008000000c004400000000400400008000000000002000000088000000200020000000000000000000000800000000c0000000000088000000000008000000020100000000002000020000000080200000000404000012000000010000200000000000000080000000000000000000000000000020000000000002000010000200000000000008000000008000000000000000000000",
      "miner" -> "0xc56",
      "difficulty" -> "0x1035EDAA0",
      "totalDifficulty" -> "0x6364D76BE9D7B",
      "extraData" -> "0xd883010607846765746887676f312e382e338664617277696e",
      "size" -> "0xC56",
      "gasLimit" -> "0x47E7C4",
      "gasUsed" -> "0xBE611",
      "timestamp" -> "0x59B4A81A",
      "transactions" -> List(
        HashMap(
          "s" -> "0x418b08924f17a5ea30b8433f1cbb08eafc445706936df89135945040258b2ce3",
          "blockHash" -> "0xacf2a4907cfbfc1b181928893c0375714fad20d4e2877b20822d55370d101c01",
          "nonce" -> "0xD334",
          "gasPrice" -> "0x6C088E200",
          "gas" -> "0x5209",
          "to" -> "0x8c3704a8612e7303eacaa5fe135482cef0c52556",
          "v" -> "0x1C",
          "hash" -> "0xe16846a4c2a0de9b4fa99b756ac4528bc8706612929dc386a27b75d4e545711e",
          "from" -> "0x81b7e08f65bdf5648606c89998a9cc8164397647",
          "blockNumber" -> "0x1919F7",
          "r" -> "0xab1d1bbd289ba86a8d2debc76389bb8bf8f592b797dbac7b568f72dcd73cdc8",
          "value" -> "0xDE0B6B3A7640000",
          "input" -> "0x",
          "transactionIndex" -> "0x0"
        ),
        HashMap(
          "s" -> "0x407387c9649c4452a75f49428d32dec9b59f773247cc12305de4d9ae88e9fa98",
          "blockHash" -> "0xacf2a4907cfbfc1b181928893c0375714fad20d4e2877b20822d55370d101c01",
          "nonce" -> "0xD335",
          "gasPrice" -> "0x6C088E200",
          "gas" -> "0x5209",
          "to" -> "0x8c3704a8612e7303eacaa5fe135482cef0c52556",
          "v" -> "0x1B",
          "hash" -> "0x0c86abbe3f4d3d1ecc5d3ef7b1acce2c780dfc923d1d0c34f95de16679d1e679",
          "from" -> "0x81b7e08f65bdf5648606c89998a9cc8164397647",
          "blockNumber" -> "0x1919F7",
          "r" -> "0xf8480c80d131a1619fe03c9c5abd219aca57cac76821c49c30bb16de2c66e403",
          "value" -> "0xDE0B6B3A7640000",
          "input" -> "0x",
          "transactionIndex" -> "0x1"
        )
      ),
      "uncles" -> List(
        "0x7a77093b82b5dff8af33954b16b51a93543d88cba8c814ad29c29f38f09e49f7",
        "0x7a28ae12786ff6cac0eb55feda9897c85a14d1fd9ad9a7c41fd1071bbc162de9"
      )
    )

    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethGetBlockByNumber(blockNumber, fullTransactionObjects)

    val actualResult = response.asInstanceOf[EthBlock].result.get.asInstanceOf[BlockWithTransactions].nonce
    val expectedResult = Utils.hex2long(rsData("nonce").toString)

    actualResult shouldBe expectedResult
  }
  it should "return information about a transaction requested by transaction hash, when invoking ethGetTransactionByHash method" in {

    val transactionHash = "0x2fdc8135dd455a8d9b29cb36d6fe7306801ea5872de941c69110c4f471fab430"
    val rq = GenericRequest(method = "eth_getTransactionByHash", params = transactionHash :: Nil)

    val rsData = HashMap(
      "hash" -> "0x2fdc8135dd455a8d9b29cb36d6fe7306801ea5872de941c69110c4f471fab430",
      "nonce" -> "0x123C2",
      "blockHash" -> "0x82af86626cae6ca7ebe2dabbb2a60c8c09af985dc39dd868ded261e0ab775554",
      "blockNumber" -> "0x19CCFC",
      "transactionIndex" -> "0x1",
      "from" -> "0x1a5d20e3957fd0b89eabf1bb95c76ab71d846ab3",
      "to" -> "0x5f81dc51bdc05f4341afbfa318af5d82c607acad",
      "value" -> "0x8E1BC9BF0400",
      "gasPrice" -> "0x4E3B29200",
      "gas" -> "0x186A0",
      "input" -> "0xb6e4d4d8000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000c000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000026494e56253246323031373039313725324658564949253246495825324631303435373735393700000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006383631303337000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000073133313534363400000000000000000000000000000000000000000000000000"
    )

    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethGetTransactionByHash(transactionHash)

    val actualResult = response.asInstanceOf[EthTransaction].result.get.nonce
    val expectedResult = Utils.hex2long(rsData("nonce"))

    actualResult shouldBe expectedResult
  }
  it should "return information about a transaction by block hash and transaction index position, when " +
    "invoking ethGetTransactionByBlockHashAndIndex method" in {

    val blockHash = "0x190b2f6fccbedaff8d86fda056703bab1d45b9a7039565f461c1cb08135173b8"
    val transactionIndex = "0x0"
    val rq = GenericRequest(method = "eth_getTransactionByBlockHashAndIndex", params = blockHash :: transactionIndex :: Nil)

    val rsData = HashMap(
      "hash" -> "0xc5d56567de1ea70bd2ca0923cf668bab2256d22ccfd37a2015f0993860893ea3",
      "nonce" -> "0x829A",
      "blockHash" -> "0x190b2f6fccbedaff8d86fda056703bab1d45b9a7039565f461c1cb08135173b8",
      "blockNumber" -> "0x19CEB1",
      "transactionIndex" -> "0x0",
      "from" -> "0x8da112ec6ded38970590ead4cc36146533157b77",
      "to" -> "0xddf57e70077d613c4bbec49f7f80e703c55d81ec",
      "value" -> "0x0",
      "gasPrice" -> "0x4E3B29200",
      "gas" -> "0x7A120",
      "input" -> "0x51a34eb800000000000000000000000000000000000000000000000da47beca5eece0000"
    )

    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethGetTransactionByBlockHashAndIndex(blockHash, transactionIndex)

    val actualResult = response.asInstanceOf[EthTransaction].result.get.nonce
    val expectedResult = Utils.hex2long(rsData("nonce"))

    actualResult shouldBe expectedResult
  }
  it should "return information about a transaction by block number and transaction index position, when " +
    "invoking ethGetTransactionByBlockNumberAndIndex method" in {

    val blockNumber  = BlockNumber(1691313)
    val block = Service.blockValue(blockNumber)
    val transactionIndex = "0x0"
    val rq = GenericRequest(method = "eth_getTransactionByBlockNumberAndIndex", params = block :: transactionIndex :: Nil)

    val rsData = HashMap(
      "hash" -> "0xc5d56567de1ea70bd2ca0923cf668bab2256d22ccfd37a2015f0993860893ea3",
      "nonce" -> "0x829A",
      "blockHash" -> "0x190b2f6fccbedaff8d86fda056703bab1d45b9a7039565f461c1cb08135173b8",
      "blockNumber" -> "0x19CEB1",
      "transactionIndex" -> "0x0",
      "from" -> "0x8da112ec6ded38970590ead4cc36146533157b77",
      "to" -> "0xddf57e70077d613c4bbec49f7f80e703c55d81ec",
      "value" -> "0x0",
      "gasPrice" -> "0x4E3B29200",
      "gas" -> "0x7A120",
      "input" -> "0x51a34eb800000000000000000000000000000000000000000000000da47beca5eece0000"
    )

    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethGetTransactionByBlockNumberAndIndex(blockNumber, transactionIndex)

    val actualResult = response.asInstanceOf[EthTransaction].result.get.nonce
    val expectedResult = Utils.hex2long(rsData("nonce"))

    actualResult shouldBe expectedResult
  }
  it should "return the receipt of a transaction by transaction hash, when invoking ethGetTransactionReceipt method" in {

    val transactionHash  = "0x2fdc8135dd455a8d9b29cb36d6fe7306801ea5872de941c69110c4f471fab430"

    val rq = GenericRequest(method = "eth_getTransactionReceipt", params = transactionHash :: Nil)

    val rsData = HashMap(
      "transactionHash" -> "0x2fdc8135dd455a8d9b29cb36d6fe7306801ea5872de941c69110c4f471fab430",
      "transactionIndex" -> "0x1",
      "blockHash" -> "0x82af86626cae6ca7ebe2dabbb2a60c8c09af985dc39dd868ded261e0ab775554",
      "blockNumber" -> "0x19CCFC",
      "root" -> "0x03e4ffd9ca5df14409457d82ddf14733424a487361d32409922a6d110cc5f403",
      "logsBloom" -> "0x00000000000000000000000000002000000000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000000000000000000000000000000020000",
      "from" -> "0x1a5d20e3957fd0b89eabf1bb95c76ab71d846ab3",
      "to" -> "0x5f81dc51bdc05f4341afbfa318af5d82c607acad",
      "cumulativeGasUsed" -> "0x1D6E7",
      "gasUsed" -> "0x132FA",
      "contractAddress" -> null,
      "logs" -> List(
        HashMap(
          "removed" -> false,
          "logIndex" -> "0x0",
          "transactionIndex" -> "0x1",
          "transactionHash" -> "0x2fdc8135dd455a8d9b29cb36d6fe7306801ea5872de941c69110c4f471fab430",
          "blockHash" -> "0x82af86626cae6ca7ebe2dabbb2a60c8c09af985dc39dd868ded261e0ab775554",
          "blockNumber" -> "0x19CCFC",
          "address" -> "0x5f81dc51bdc05f4341afbfa318af5d82c607acad",
          "data" -> "0x000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000c000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000026494e56253246323031373039313725324658564949253246495825324631303435373735393700000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006383631303337000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000073133313534363400000000000000000000000000000000000000000000000000",
          "topics" -> List(
            "0x662fd29da6ea3246128acda274b24aed94859deaafb7b415fdfa765f69f5dd83"
          )
        )
      )
    )

    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethGetTransactionReceipt(transactionHash)

    val actualResult = response.asInstanceOf[EthTransactionReceipt].result.get.gasUsed
    val expectedResult = Utils.hex2long(rsData("gasUsed").toString)

    actualResult shouldBe expectedResult
  }
  it should "return information about an uncle of a block by hash and uncle index position, when " +
    "invoking ethGetUncleByBlockHashAndIndex method" in {

    val blockHash  = "0xacf2a4907cfbfc1b181928893c0375714fad20d4e2877b20822d55370d101c01"
    val uncleIndex = "0x0"

    val rq = GenericRequest(method = "eth_getUncleByBlockHashAndIndex", params = blockHash :: uncleIndex :: Nil)

    val rsData = HashMap(
      "number" -> "0x1919F6",
      "hash" -> "0x7a77093b82b5dff8af33954b16b51a93543d88cba8c814ad29c29f38f09e49f7",
      "parentHash" -> "0x5cc0d59d11bb64090ad3e1c832526c9640702d5a896d15627a2a5361f3a1218f",
      "mixHash" -> "0x7846d296cc5d3cd42279dc427f3d69fb0804d9a04857d47fc4522695e931cf52",
      "nonce" -> "0x9f7aaa9401bf786f",
      "transactionsRoot" -> "0x4278e6ec961c8d890a4edc11242d15378be7d92e67970f324ebea02319e50420",
      "stateRoot" -> "0x2dfcddf3c2b07bfa9f68b76b520472e44a4c50d67d84092e916441c6b51a7d3d",
      "receiptsRoot" -> "0xa3c41dbb018f8a7dc8206cc9ea52cfebc03e84eb41d9323baddc074fe700c170",
      "sha3Uncles" -> "0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
      "logsBloom" -> "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000000000000000000000100000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000020000000000000000000000000010000000000000000000000000000000000000010000000000040000000000100400000000000000000000000",
      "miner" -> "0x213",
      "difficulty" -> "0x1037F0A81",
      "totalDifficulty" -> "0x6364C735FC2DB",
      "extraData" -> "0x526f707374656e20506f6f6c",
      "size" -> "0x213",
      "gasLimit" -> "0x47E7C4",
      "gasUsed" -> "0x2D716",
      "timestamp" -> "0x59B4A7FF",
      "transactions" -> List.empty[String],
      "uncles" -> List.empty[String]
    )

    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethGetUncleByBlockHashAndIndex(blockHash, uncleIndex)

    val actualResult = response.asInstanceOf[EthBlock].result.get.asInstanceOf[BlockWithoutTransactions].nonce
    val expectedResult = Utils.hex2bigint(rsData("nonce").toString)

    actualResult shouldBe expectedResult
  }
  it should "return information about an uncle of a block by number and uncle index position, when " +
    "invoking ethGetUncleByBlockNumberAndIndex method" in {

    val blockNumber  = BlockNumber(1692292)
    val uncleIndex = "0x1"

    val rq = GenericRequest(method = "eth_getUncleByBlockNumberAndIndex", params = blockNumber :: uncleIndex :: Nil)

    val rsData = HashMap(
      "number" -> "0x19D282",
      "hash" -> "0xa4363c8501f337a47cfa610c13b219e8125005dc90a5575e0e12738a556cad7d",
      "parentHash" -> "0x588ea98dc6e0628855bcb951f93104c8a2e71f4ad337aa5280d7532df2b9e834",
      "mixHash" -> "0xf1ac58179528a38af0dec308149a8934638bc5c76dfb64be306f6ef63d4237c9",
      "nonce" -> "0x8b846337093d26b6",
      "transactionsRoot" -> "0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      "stateRoot" -> "0x47a7d44425edd24fdfd1aeb6acbd6f5d15dc0bc79fe2c8f86e18256a540eb5aa",
      "receiptsRoot" -> "0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
      "sha3Uncles" -> "0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
      "logsBloom" -> "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      "miner" -> "0x210",
      "difficulty" -> "0x291ACFB0C",
      "totalDifficulty" -> "null",
      "extraData" -> "0x526f707374656e20506f6f6c",
      "size" -> "0x210",
      "gasLimit" -> "0x47E7C4",
      "gasUsed" -> "0x0",
      "timestamp" -> "0x59BEC39A",
      "transactions" -> List.empty[String],
      "uncles" -> List.empty[String]
    )

    val rs = GenericResponse("2.0", 33, None, Some(rsData))

    val response = service(rq, rs).ethGetUncleByBlockNumberAndIndex(blockNumber, uncleIndex)

    val actualResult = response.asInstanceOf[EthBlock].result.get.asInstanceOf[BlockWithoutTransactions].nonce
    val expectedResult = Utils.hex2bigint(rsData("nonce").toString)

    actualResult shouldBe expectedResult
  }

}
