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

  implicit val formats: DefaultFormats.type = DefaultFormats

  var httpClientMock: JValueHttpClient = _
  var jsonMapperMock: JsonMapper = _

  before {
    httpClientMock = mock[DispatchHttpClient]
    jsonMapperMock = mock[JacksonJsonMapper]
  }

  private def service(rq: Request, rs: GenericResponse) = {

    val byteRequest = jsonMapperMock.writeAsBytes(rq)
    val jsonResponse = Extraction.decompose(rs)

    when(httpClientMock.sync(byteRequest)).thenReturn(jsonResponse)

    new Service(jsonMapperMock, httpClientMock)
  }

  "Service" should "return current Ethereum client version, when invoking web3ClientVersion method" in {

    val rq = Request(method = "web3_clientVersion")
    val rs = GenericResponse("2.0", 33, None, "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3")

    val response = service(rq, rs).web3ClientVersion

    response.asInstanceOf[SuccessString].result shouldBe "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3"
  }
  it should "return Keccak-256 of the given data, when invoking web3Sha3 method with valid input" in {

    val data = Utils.int2hex(123)
    val rq = Request(method = "web3_sha3", params = data :: Nil)
    val rs = GenericResponse("2.0", 33, None, "0xa91eddf639b0b768929589c1a9fd21dcb0107199bdd82e55c5348018a1572f52")

    val response = service(rq, rs).web3Sha3(data)

    response.asInstanceOf[SuccessString].result shouldBe
      "0xa91eddf639b0b768929589c1a9fd21dcb0107199bdd82e55c5348018a1572f52"
  }
  it should "return Error object, when invoking web3Sha3 method with invalid input" in {

    val data = "test"
    val rq = Request(method = "web3_sha3", params = data :: Nil)
    val rs = GenericResponse("2.0", 33,
      Some(
        ErrorContent(-32602,
          "invalid argument 0: json: cannot unmarshal hex string without 0x prefix into Go value of type hexutil.Bytes"
        )
      ), AnyRef
    )

    val response = service(rq, rs).web3Sha3(data)

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32602
    response.asInstanceOf[Error].error.message shouldBe
      "invalid argument 0: json: cannot unmarshal hex string without 0x prefix into Go value of type hexutil.Bytes"
  }
  it should "return current network id, when invoking netVersion method" in {

    val rq = Request(method = "net_version")
    val rs = GenericResponse("2.0", 33, None, "3")

    val response = service(rq, rs).netVersion

    response.asInstanceOf[SuccessString].result shouldBe "3"
  }
  it should "return true if client is actively listening for network connections, when invoking netListening method" in {

    val rq = Request(method = "net_listening")
    val rs = GenericResponse("2.0", 33, None, true)

    val response = service(rq, rs).netListening

    response.asInstanceOf[SuccessBoolean].result shouldBe true
  }
  it should "return number of peers currently connected to the client, when invoking netPeerCount method" in {

    val rq = Request(method = "net_peerCount")
    val rs = GenericResponse("2.0", 33, None, "0xB")

    val response = service(rq, rs).netPeerCount

    Utils.hex2long(response.asInstanceOf[SuccessString].result) shouldBe 11
  }
  it should "return the current ethereum protocol version, when invoking ethProtocolVersion method" in {

    val rq = Request(method = "eth_protocolVersion")
    val rs = GenericResponse("2.0", 33, None, "0x3F")

    val response = service(rq, rs).ethProtocolVersion

    Utils.hex2long(response.asInstanceOf[SuccessString].result) shouldBe 63
  }
  it should "return false, when invoking ethSyncing method and synchronization with blockchain is not taking place" in {

    val rq = Request(method = "eth_syncing")
    val rs = GenericResponse("2.0", 33, None, false)

    val response = service(rq, rs).ethSyncing

    response.asInstanceOf[SuccessBoolean].result shouldBe false
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

    val result = response.asInstanceOf[SuccessMap].result
    result.map {
      x => (x._1, Utils.hex2long(x._2.toString))
    }.mkString(", ") shouldBe
      "pulledStates -> 4871, knownStates -> 9511, currentBlock -> 120384, highestBlock -> 834763, startingBlock -> 120384"
  }
  it should "return the client coinbase address, when invoking ethCoinbase method" in {

    val rq = Request(method = "eth_coinbase")
    val rs = GenericResponse("2.0", 33, None, "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617")

    val response = service(rq, rs).ethCoinbase

    response.asInstanceOf[SuccessString].result shouldBe "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
  }
  it should "return true if client is actively mining new blocks, when invoking ethMining method" in {

    val rq = Request(method = "eth_mining")
    val rs = GenericResponse("2.0", 33, None, false)

    val response = service(rq, rs).ethMining

    response.asInstanceOf[SuccessBoolean].result shouldBe false
  }
  it should "return the number of hashes per second that the node is mining with, when invoking ethHashrate method" in {

    val rq = Request(method = "eth_hashrate")
    val rs = GenericResponse("2.0", 33, None, "0x0")

    val response = service(rq, rs).ethHashrate

    Utils.hex2long(response.asInstanceOf[SuccessString].result) shouldBe 0
  }
  it should "return the current price per gas in wei, when invoking ethGasPrice method" in {

    val rq = Request(method = "eth_gasPrice")
    val rs = GenericResponse("2.0", 33, None, "0x6FC23AC00")

    val response = service(rq, rs).ethGasPrice

    Utils.hex2long(response.asInstanceOf[SuccessString].result) shouldBe 30000000000L
  }

}
