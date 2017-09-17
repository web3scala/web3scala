package org.web3scala.json

import org.json4s.{DefaultFormats, JValue, Reader}
import org.web3scala.model._
import org.web3scala.util.Utils

object JacksonReaders {
  implicit val formats: DefaultFormats.type = DefaultFormats
  implicit object GenericResponseReader extends Reader[GenericResponse] {
    def read(json: JValue): GenericResponse = GenericResponse(
      (json \ "jsonrpc").extract[String],
      (json \ "id").extract[Int],
      (json \ "error").extractOpt[ErrorContent],
      (json \ "result").extractOpt[Any]
    )
  }
  implicit object ErrorContentReader extends Reader[ErrorContent] {
    def read(json: JValue): ErrorContent = ErrorContent(
      (json \ "code").extract[Int],
      (json \ "message").extract[String]
    )
  }
  implicit object BlockWithoutTransactionsReader extends Reader[BlockWithoutTransactions] {
    def read(json: JValue): BlockWithoutTransactions = BlockWithoutTransactions(
      Utils.hex2long((json \ "number").extract[String]),
      (json \ "hash").extract[String],
      (json \ "parentHash").extract[String],
      (json \ "mixHash").extract[String],
      (json \ "nonce").extract[String],
      (json \ "transactionsRoot").extract[String],
      (json \ "stateRoot").extract[String],
      (json \ "receiptsRoot").extract[String],
      (json \ "sha3Uncles").extract[String],
      (json \ "logsBloom").extract[String],
      (json \ "size").extract[String],
      Utils.hex2long((json \ "difficulty").extract[String]),
      Utils.hex2long((json \ "totalDifficulty").extract[String]),
      (json \ "extraData").extract[String],
      Utils.hex2long((json \ "size").extract[String]),
      Utils.hex2long((json \ "gasLimit").extract[String]),
      Utils.hex2long((json \ "gasUsed").extract[String]),
      Utils.hex2long((json \ "timestamp").extract[String]),
      (json \ "transactions").extract[List[String]],
      (json \ "uncles").extract[List[String]]
    )
  }
  implicit object BlockWithTransactionsReader extends Reader[BlockWithTransactions] {
    def read(json: JValue): BlockWithTransactions = BlockWithTransactions(
      Utils.hex2long((json \ "number").extract[String]),
      (json \ "hash").extract[String],
      (json \ "parentHash").extract[String],
      (json \ "mixHash").extract[String],
      (json \ "nonce").extract[String],
      (json \ "transactionsRoot").extract[String],
      (json \ "stateRoot").extract[String],
      (json \ "receiptsRoot").extract[String],
      (json \ "sha3Uncles").extract[String],
      (json \ "logsBloom").extract[String],
      (json \ "size").extract[String],
      Utils.hex2long((json \ "difficulty").extract[String]),
      Utils.hex2long((json \ "totalDifficulty").extract[String]),
      (json \ "extraData").extract[String],
      Utils.hex2long((json \ "size").extract[String]),
      Utils.hex2long((json \ "gasLimit").extract[String]),
      Utils.hex2long((json \ "gasUsed").extract[String]),
      Utils.hex2long((json \ "timestamp").extract[String]),
      (json \ "transactions").children.map(x => x.as[BlockTransaction](BlockTransactionReader, manifest[BlockTransaction])),
      (json \ "uncles").extract[List[String]]
    )
  }
  implicit object BlockTransactionReader extends Reader[BlockTransaction] {
    def read(json: JValue): BlockTransaction = BlockTransaction(
      (json \ "s").extract[String],
      (json \ "blockHash").extract[String],
      Utils.hex2long((json \ "nonce").extract[String]),
      Utils.hex2long((json \ "gasPrice").extract[String]),
      Utils.hex2long((json \ "gas").extract[String]),
      (json \ "to").extract[String],
      Utils.hex2long((json \ "v").extract[String]),
      (json \ "hash").extract[String],
      (json \ "from").extract[String],
      Utils.hex2long((json \ "blockNumber").extract[String]),
      (json \ "r").extract[String],
      Utils.hex2long((json \ "value").extract[String]),
      (json \ "input").extract[String],
      Utils.hex2long((json \ "transactionIndex").extract[String])
    )
  }
  implicit object TransactionReader extends Reader[Transaction] {
    def read(json: JValue): Transaction = Transaction(
      (json \ "hash").extract[String],
      Utils.hex2long((json \ "nonce").extract[String]),
      (json \ "blockHash").extract[String],
      Utils.hex2long((json \ "blockNumber").extract[String]),
      Utils.hex2long((json \ "transactionIndex").extract[String]),
      (json \ "from").extract[String],
      (json \ "to").extract[String],
      Utils.hex2long((json \ "value").extract[String]),
      Utils.hex2long((json \ "gasPrice").extract[String]),
      Utils.hex2long((json \ "gas").extract[String]),
      (json \ "input").extract[String]
    )
  }
}