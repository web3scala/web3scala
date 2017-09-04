package org.web3scala

import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest._
import Matchers._
import org.web3scala.model._
import org.web3scala.protocol.Ethereum
import org.web3scala.util.Utils
import scala.collection.immutable.HashMap

class ServiceSpec extends FlatSpec with BeforeAndAfter with MockitoSugar {

  var serviceMock: Ethereum = _

  before {
    serviceMock = mock[Ethereum]
  }

  "Synchronously, Service" should "return current Ethereum client version, when invoking web3ClientVersion method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3")
    when(serviceMock.web3ClientVersion).thenReturn(sampleResponse)
    val rawResponse = serviceMock.web3ClientVersion
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[SuccessString].result shouldBe "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3"
  }
  it should "return Keccak-256 of the given data, when invoking web3Sha3 method with valid input" in {
    val sampleData = Utils.int2hex(123)
    val sampleResponse = GenericResponse(
      "2.0", 33, None, "0xa91eddf639b0b768929589c1a9fd21dcb0107199bdd82e55c5348018a1572f52"
    )
    when(serviceMock.web3Sha3(sampleData)).thenReturn(sampleResponse)
    val rawResponse = serviceMock.web3Sha3(sampleData)
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[SuccessString].result shouldBe "0xa91eddf639b0b768929589c1a9fd21dcb0107199bdd82e55c5348018a1572f52"
  }
  it should "return Error object, when invoking web3Sha3 method with invalid input" in {
    val sampleData = "test"
    val sampleResponse = GenericResponse(
      "2.0", 33, Some(ErrorContent(
        -32602,
        "invalid argument 0: json: cannot unmarshal hex string without 0x prefix into Go value of type hexutil.Bytes"
      )), AnyRef)
    when(serviceMock.web3Sha3(sampleData)).thenReturn(sampleResponse)
    val rawResponse = serviceMock.web3Sha3(sampleData)
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
    response.asInstanceOf[Error].error.code shouldBe -32602
    response.asInstanceOf[Error].error.message shouldBe "invalid argument 0: json: cannot unmarshal hex string without 0x prefix into Go value of type hexutil.Bytes"
  }
  it should "return current network id, when invoking netVersion method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, "3")
    when(serviceMock.netVersion).thenReturn(sampleResponse)
    val rawResponse = serviceMock.netVersion
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[SuccessString].result shouldBe "3"
  }
  it should "return true if client is actively listening for network connections, when invoking netListening method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, true)
    when(serviceMock.netListening).thenReturn(sampleResponse)
    val rawResponse = serviceMock.netListening
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[SuccessBoolean].result shouldBe true
  }
  it should "return number of peers currently connected to the client, when invoking netPeerCount method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, "0xB")
    when(serviceMock.netPeerCount).thenReturn(sampleResponse)
    val rawResponse = serviceMock.netPeerCount
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    Utils.hex2long(response.asInstanceOf[SuccessString].result) shouldBe 11
  }
  it should "return the current ethereum protocol version, when invoking ethProtocolVersion method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, "0x3F")
    when(serviceMock.ethProtocolVersion).thenReturn(sampleResponse)
    val rawResponse = serviceMock.ethProtocolVersion
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    Utils.hex2long(response.asInstanceOf[SuccessString].result) shouldBe 63
  }
  it should "return false, when invoking ethSyncing method and synchronization with blockchain is not taking place" in {
    val sampleResponse = GenericResponse("2.0", 33, None, false)
    when(serviceMock.ethSyncing).thenReturn(sampleResponse)
    val rawResponse = serviceMock.ethSyncing
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[SuccessBoolean].result shouldBe false
  }
  it should "return an object with data about the sync status, when invoking ethSyncing method and synchronization with blockchain is taking place" in {
    val sampleResponseElement = HashMap(
      ("pulledStates", "0x1307"),
      ("knownStates", "0x2527"),
      ("currentBlock", "0x1d640"),
      ("highestBlock", "0xcbccb"),
      ("startingBlock", "0x1d640")
    )
    val sampleResponse = GenericResponse("2.0", 33, None, sampleResponseElement)
    when(serviceMock.ethSyncing).thenReturn(sampleResponse)
    val rawResponse = serviceMock.ethSyncing
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])
    val result = response.asInstanceOf[SuccessMap].result

    result.map {
      x => (x._1, Utils.hex2long(x._2.toString))
    }.mkString(", ") shouldBe
      "pulledStates -> 4871, knownStates -> 9511, currentBlock -> 120384, highestBlock -> 834763, startingBlock -> 120384"
  }
  it should "return the client coinbase address, when invoking ethCoinbase method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617")
    when(serviceMock.ethCoinbase).thenReturn(sampleResponse)
    val rawResponse = serviceMock.ethCoinbase
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[SuccessString].result shouldBe "0x1f2e3994505ea24642d94d00a4bcf0159ed1a617"
  }
  it should "return true if client is actively mining new blocks, when invoking ethMining method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, false)
    when(serviceMock.ethMining).thenReturn(sampleResponse)
    val rawResponse = serviceMock.ethMining
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[SuccessBoolean].result shouldBe false
  }
  it should "return the number of hashes per second that the node is mining with, when invoking ethHashrate method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, "0x0")
    when(serviceMock.ethHashrate).thenReturn(sampleResponse)
    val rawResponse = serviceMock.ethHashrate
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    Utils.hex2long(response.asInstanceOf[SuccessString].result) shouldBe 0
  }
  it should "return the current price per gas in wei, when invoking ethGasPrice method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, "0x6FC23AC00")
    when(serviceMock.ethGasPrice).thenReturn(sampleResponse)
    val rawResponse = serviceMock.ethGasPrice
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    Utils.hex2long(response.asInstanceOf[SuccessString].result) shouldBe 30000000000L
  }

}
