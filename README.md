# web3scala
_web3scala_ allows seamless integration with [Ethereum](https://www.ethereum.org) blockchain, using Scala programming 
language.

Lightweight, efficient, using Scala idioms, it spares you the trouble of writing own low-level code controlling the 
work of Ethereum nodes.  


## Getting started

#### SBT

    $ sbt package

#### Ethereum client

    $ geth --rpcapi personal,db,eth,net,web3 --rpc --testnet

#### Sending requests

```scala
  val service = new Service
  
  // synchronous call
  service.web3ClientVersion match {
    case s: SuccessString => println("Client Version: " + s.result)
    case e: Error => println("Client Version: " + e.error)
  }

  // asynchronous call
  val rs = service.asyncWeb3ClientVersion.future()
  service.handleResponse(rs.as[GenericResponse]) match {
    case s: SuccessString => println("Client Version: " + s.result)
    case e: Error => println("Client Version: " + e.error)
  }
```

#### Stacking futures

Assuming you have three Ethereum wallets:

```scala
  val rq1 = ("0x1f2e3994505ea24642d94d00a4bcf0159ed1a617", BlockName("latest"))
  val rq2 = ("0xf9C510e90bCb47cc49549e57b80814aE3A8bb683", BlockName("pending"))
  val rq3 = ("0x902c4fD71e196E86e7C82126Ff88ADa63a590d22", BlockNumber(1559297))

```

and want to compare their balances, choosing one with most ether in it:

```scala
  val result = highestBalance(rq1, rq2, rq3)

  println("Highest Balance: " + result())
```

You want the code written to be non-blocking on I/O at any point, and http requests execution to be fully parallelized:

```scala
   val service = new Service
 
   implicit val formats: DefaultFormats.type = DefaultFormats
 
   def highestBalance(requestParams: (String, Block)*): Future[String] = {
 
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

```


## Dependencies

The library has following runtime dependencies:

* [Dispatch Reboot](https://dispatchhttp.org) for asynchronous HTTP interaction
* [Json4s-Jackson](http://json4s.org) for JSON parsing/generation
* [jackson-module-scala](https://github.com/FasterXML/jackson-module-scala) to support Scala-specific datatypes