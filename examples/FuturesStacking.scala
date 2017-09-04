import dispatch._
import Defaults._
import org.json4s.DefaultFormats
import org.web3scala.Service
import org.web3scala.model.{Block, BlockName, BlockNumber}
import org.web3scala.util.Utils

object FuturesStacking extends App {

  val service = new Service

  implicit val formats: DefaultFormats.type = DefaultFormats

  /** Returns single winner with highest Ether balance, among addresses passed to the method */
  def highestBalance(requestParams: (String, Block)*) = {

    // execute async requests
    val responses =
      for (requestParam <- requestParams)
        yield requestParam._1 -> service.asyncEthGetBalance(requestParam._1, requestParam._2)

    // parse responses
    val futures =
      for (response <- responses)
        yield for (json <- response._2.future)
          yield response._1 -> Utils.hex2long((json \ "result").extract[String])

    // select max balance and return corresponding address
    for (future <- Future.sequence(futures))
      yield future.maxBy(_._2)._1
  }

  val rq1 = ("0x1f2e3994505ea24642d94d00a4bcf0159ed1a617", BlockName("latest"))
  val rq2 = ("0xf9C510e90bCb47cc49549e57b80814aE3A8bb683", BlockName("pending"))
  val rq3 = ("0x902c4fD71e196E86e7C82126Ff88ADa63a590d22", BlockNumber(1559297))

  val result = highestBalance(rq1, rq2, rq3)

  println("Highest Balance: " + result())

  System.exit(0)
}