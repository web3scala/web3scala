package org.web3scala

import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest._
import Matchers._
import org.web3scala.model._
import org.web3scala.protocol.Ethereum
import org.web3scala.util.Utils

class ServiceSpec extends FlatSpec with BeforeAndAfter with MockitoSugar {

  var serviceMock: Ethereum = _

  before {
    serviceMock = mock[Ethereum]
  }

  "A Service" should "return current Ethereum client version, when invoking web3ClientVersion method" in {
    val sampleResponse = GenericResponse("2.0", 33, None, "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3")
    when(serviceMock.web3ClientVersion).thenReturn(sampleResponse)
    val rawResponse = serviceMock.web3ClientVersion
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[SuccessString].result shouldBe "Geth/v1.6.7-stable-ab5646c5/darwin-amd64/go1.8.3"
  }
  it should "return Error object, when invoking web3ClientVersion method" in {
    val sampleResponse = GenericResponse("2.0", 33, Some(ErrorContent(0, "")), AnyRef)
    when(serviceMock.web3ClientVersion).thenReturn(sampleResponse)
    val rawResponse = serviceMock.web3ClientVersion
    val response = Service.handleResponse(rawResponse.asInstanceOf[GenericResponse])

    response.asInstanceOf[Error].error shouldBe a [ErrorContent]
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

}
