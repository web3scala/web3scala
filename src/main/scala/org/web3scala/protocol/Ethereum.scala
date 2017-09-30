package org.web3scala.protocol

import org.web3scala.model._

trait Ethereum {

  /** Returns current client version */
  def web3ClientVersion: Either[Error, Response]

  /** Returns asynchronously current client version */
  def asyncWeb3ClientVersion: AsyncResponse

  /** Returns Keccak-256 (not the standardized SHA3-256) of the given data */
  def web3Sha3(data: String): Either[Error, Response]

  /** Returns asynchronously Keccak-256 (not the standardized SHA3-256) of the given data */
  def asyncWeb3Sha3(data: String): AsyncResponse

  /**
    * Returns the current network id
    *  1: Ethereum Mainnet
    *  2: Morden Testnet (deprecated)
    *  3: Ropsten Testnet
    *  4: Rinkeby Testnet
    * 42: Kovan Testnet
    */
  def netVersion: Either[Error, Response]

  /**
    * Returns asynchronously the current network id
    *  1: Ethereum Mainnet
    *  2: Morden Testnet (deprecated)
    *  3: Ropsten Testnet
    *  4: Rinkeby Testnet
    * 42: Kovan Testnet
    */
  def asyncNetVersion: AsyncResponse

  /** Returns true if client is actively listening for network connections */
  def netListening: Either[Error, Response]

  /** Returns asynchronously true if client is actively listening for network connections */
  def asyncNetListening: AsyncResponse

  /** Returns number of peers currently connected to the client */
  def netPeerCount: Either[Error, Response]

  /** Returns asynchronously number of peers currently connected to the client */
  def asyncNetPeerCount: AsyncResponse

  /** Returns the current ethereum protocol version */
  def ethProtocolVersion: Either[Error, Response]

  /** Returns asynchronously the current ethereum protocol version */
  def asyncEthProtocolVersion: AsyncResponse

  /** Returns an object with data about the sync status or false */
  def ethSyncing: Either[Error, Response]

  /** Returns asynchronously an object with data about the sync status or false */
  def asyncEthSyncing: AsyncResponse

  /** Returns the client coinbase address */
  def ethCoinbase: Either[Error, Response]

  /** Returns asynchronously the client coinbase address */
  def asyncEthCoinbase: AsyncResponse

  /** Returns true if client is actively mining new blocks */
  def ethMining: Either[Error, Response]

  /** Returns asynchronously true if client is actively mining new blocks */
  def asyncEthMining: AsyncResponse

  /** Returns the number of hashes per second that the node is mining with */
  def ethHashrate: Either[Error, Response]

  /** Returns asynchronously the number of hashes per second that the node is mining with */
  def asyncEthHashrate: AsyncResponse

  /** Returns the current price per gas in wei */
  def ethGasPrice: Either[Error, Response]

  /** Returns asynchronously the current price per gas in wei */
  def asyncEthGasPrice: AsyncResponse

  /** Returns a list of addresses owned by client */
  def ethAccounts: Either[Error, Response]

  /** Returns asynchronously a list of addresses owned by client */
  def asyncEthAccounts: AsyncResponse

  /** Returns the number of most recent block */
  def ethBlockNumber: Either[Error, Response]

  /** Returns asynchronously the number of most recent block */
  def asyncEthBlockNumber: AsyncResponse

  /** Returns the balance of the account of given address */
  def ethGetBalance(address: String, defaultBlock: BlockType): Either[Error, Response]

  /** Returns asynchronously the balance of the account of given address */
  def asyncEthGetBalance(address: String, defaultBlock: BlockType): AsyncResponse

  /** Returns the value from a storage position at a given address */
  def ethGetStorageAt(address: String, position: String, defaultBlock: BlockType): Either[Error, Response]

  /** Returns asynchronously the value from a storage position at a given address */
  def asyncEthGetStorageAt(address: String, position: String, defaultBlock: BlockType): AsyncResponse

  /** Returns the number of transactions sent from an address */
  def ethGetTransactionCount(address: String, defaultBlock: BlockType): Either[Error, Response]

  /** Returns asynchronously the number of transactions sent from an address */
  def asyncEthGetTransactionCount(address: String, defaultBlock: BlockType): AsyncResponse

  /** Returns the number of transactions in a block from a block matching the given block hash */
  def ethGetBlockTransactionCountByHash(blockHash: String): Either[Error, Response]

  /** Returns asynchronously the number of transactions in a block from a block matching the given block hash */
  def asyncEthGetBlockTransactionCountByHash(blockHash: String): AsyncResponse

  /** Returns the number of transactions in a block from a block matching the given block number */
  def ethGetBlockTransactionCountByNumber(defaultBlock: BlockType): Either[Error, Response]

  /** Returns asynchronously the number of transactions in a block from a block matching the given block number */
  def asyncEthGetBlockTransactionCountByNumber(defaultBlock: BlockType): AsyncResponse

  /** Returns the number of uncles in a block from a block matching the given block hash */
  def ethGetUncleCountByBlockHash(blockHash: String): Either[Error, Response]

  /** Returns asynchronously the number of uncles in a block from a block matching the given block hash */
  def asyncEthGetUncleCountByBlockHash(blockHash: String): AsyncResponse

  /** Returns the number of uncles in a block from a block matching the given block number */
  def ethGetUncleCountByBlockNumber(defaultBlock: BlockType): Either[Error, Response]

  /** Returns asynchronously the number of uncles in a block from a block matching the given block number */
  def asyncEthGetUncleCountByBlockNumber(defaultBlock: BlockType): AsyncResponse

  /** Returns code at a given address */
  def ethGetCode(address: String, defaultBlock: BlockType): Either[Error, Response]

  /** Returns asynchronously code at a given address */
  def asyncEthGetCode(address: String, defaultBlock: BlockType): AsyncResponse

  /**
    * Returns calculated Ethereum specific signature with:
    * sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message)))
    *
    * Adding a prefix to the message makes the calculated signature recognisable as an Ethereum
    * specific signature. This prevents misuse where a malicious DApp can sign arbitrary data
    * (e.g. transaction) and use the signature to impersonate the victim.
    *
    * Note: the address to sign with must be unlocked.
    */
  def ethSign(address: String, message: String): Either[Error, Response]

  /**
    * Returns asynchronously calculated Ethereum specific signature with:
    * sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message)))
    *
    * Adding a prefix to the message makes the calculated signature recognisable as an Ethereum
    * specific signature. This prevents misuse where a malicious DApp can sign arbitrary data
    * (e.g. transaction) and use the signature to impersonate the victim.
    *
    * Note: the address to sign with must be unlocked.
    */
  def asyncEthSign(address: String, message: String): AsyncResponse


  /** Creates new message call transaction or a contract creation, if the data field contains code */
  def ethSendTransaction(obj: EthSendTransactionObject): Either[Error, Response]

  /** Creates asynchronously new message call transaction or a contract creation, if the data field contains code */
  def asyncEthSendTransaction(obj: EthSendTransactionObject): AsyncResponse

  /** Creates new message call transaction or a contract creation for signed transactions */
  def ethSendRawTransaction(signedTransactionData: String): Either[Error, Response]

  /** Creates asynchronously new message call transaction or a contract creation for signed transactions */
  def asyncEthSendRawTransaction(signedTransactionData: String): AsyncResponse

  /** Executes a new message call immediately without creating a transaction on the block chain */
  def ethCall(obj: EthCallObject, defaultBlock: BlockType): Either[Error, Response]

  /** Executes asynchronously a new message call immediately without creating a transaction on the block chain */
  def asyncEthCall(obj: EthCallObject, defaultBlock: BlockType): AsyncResponse

  /**
    * Makes a call or transaction, which won't be added to the blockchain and returns the used gas, which
    * can be used for estimating the used gas
    */
  def ethEstimateGas(obj: EthEstimateGasObject): Either[Error, Response]

  /**
    * Makes an asynchronous call or transaction, which won't be added to the blockchain and returns the used
    * gas, which can be used for estimating the used gas
    */
  def asyncEthEstimateGas(obj: EthEstimateGasObject): AsyncResponse

  /** Returns information about a block by hash */
  def ethGetBlockByHash(blockHash: String, fullTransactionObjects: Boolean): Either[Error, Response]

  /** Returns asynchronously information about a block by hash */
  def asyncEthGetBlockByHash(blockHash: String, fullTransactionObjects: Boolean): AsyncResponse

  /** Returns information about a block by block number */
  def ethGetBlockByNumber(defaultBlock: BlockType, fullTransactionObjects: Boolean): Either[Error, Response]

  /** Returns asynchronously information about a block by block number */
  def asyncEthGetBlockByNumber(defaultBlock: BlockType, fullTransactionObjects: Boolean): AsyncResponse

  /** Returns information about a transaction requested by transaction hash */
  def ethGetTransactionByHash(transactionHash: String): Either[Error, Response]

  /** Returns asynchronously information about a transaction requested by transaction hash */
  def asyncEthGetTransactionByHash(transactionHash: String): AsyncResponse

  /** Returns information about a transaction by block hash and transaction index position */
  def ethGetTransactionByBlockHashAndIndex(blockHash: String, transactionIndex: String): Either[Error, Response]

  /** Returns asynchronously information about a transaction by block hash and transaction index position */
  def asyncEthGetTransactionByBlockHashAndIndex(blockHash: String, transactionIndex: String): AsyncResponse

  /** Returns information about a transaction by block number and transaction index position */
  def ethGetTransactionByBlockNumberAndIndex(defaultBlock: BlockType, transactionIndex: String): Either[Error, Response]

  /** Returns asynchronously information about a transaction by block number and transaction index position */
  def asyncEthGetTransactionByBlockNumberAndIndex(defaultBlock: BlockType, transactionIndex: String): AsyncResponse

  /** Returns the receipt of a transaction by transaction hash (receipts are not available for pending transactions */
  def ethGetTransactionReceipt(transactionHash: String): Either[Error, Response]

  /** Returns asynchronously the receipt of a transaction by transaction hash (receipts are not available for pending transactions */
  def asyncEthGetTransactionReceipt(transactionHash: String): AsyncResponse

  /**
    * Returns information about an uncle of a block by hash and uncle index position
    * Note: An uncle doesn't contain individual transactions
    */
  def ethGetUncleByBlockHashAndIndex(blockHash: String, uncleIndex: String): Either[Error, Response]

  /**
    * Returns asynchronously information about an uncle of a block by hash and uncle index position
    * Note: An uncle doesn't contain individual transactions
    */
  def asyncEthGetUncleByBlockHashAndIndex(blockHash: String, uncleIndex: String): AsyncResponse

  /**
    * Returns information about an uncle of a block by number and uncle index position
    * Note: An uncle doesn't contain individual transactions
    */
  def ethGetUncleByBlockNumberAndIndex(defaultBlock: BlockType, uncleIndex: String): Either[Error, Response]

  /**
    * Returns asynchronously information about an uncle of a block by number and uncle index position
    * Note: An uncle doesn't contain individual transactions
    */
  def asyncEthGetUncleByBlockNumberAndIndex(defaultBlock: BlockType, uncleIndex: String): AsyncResponse

  /** Creates a filter object, based on filter options, to notify when the state changes (logs) */
  def ethNewFilter(obj: EthNewFilterObject): Either[Error, Response]

  /** Creates asynchronously a filter object, based on filter options, to notify when the state changes (logs) */
  def asyncEthNewFilter(obj: EthNewFilterObject): AsyncResponse

  /** Creates a filter in the node, to notify when a new block arrives */
  def ethNewBlockFilter: Either[Error, Response]

  /** Creates asynchronously a filter in the node, to notify when a new block arrives */
  def asyncEthNewBlockFilter: AsyncResponse

  /** Creates a filter in the node, to notify when new pending transactions arrive */
  def ethNewPendingTransactionFilter: Either[Error, Response]

  /** Creates asynchronously a filter in the node, to notify when new pending transactions arrive */
  def asyncEthNewPendingTransactionFilter: AsyncResponse

  /** Uninstalls a filter with given id. Should always be called when watch is no longer needed */
  def ethUninstallFilter(id: String): Either[Error, Response]

  /** Uninstalls asynchronously a filter with given id. Should always be called when watch is no longer needed */
  def asyncEthUninstallFilter(id: String): AsyncResponse

  /** Polling method for a filter, which returns an array of logs which occurred since last poll */
  def ethGetFilterChanges(id: String): Either[Error, Response]

  /** Asynchronous polling method for a filter, which returns an array of logs which occurred since last poll */
  def asyncEthGetFilterChanges(id: String): AsyncResponse

  /** Returns an array of all logs matching filter with given id */
  def ethGetFilterLogs(id: String): Either[Error, Response]

  /** Returns asynchronously an array of all logs matching filter with given id */
  def asyncEthGetFilterLogs(id: String): AsyncResponse

  /** Returns an array of all logs matching a given filter object */
  def ethGetLogs(obj: EthNewFilterObject): Either[Error, Response]

  /** Returns asynchronously an array of all logs matching a given filter object */
  def asyncEthGetLogs(obj: EthNewFilterObject): AsyncResponse

  /** Returns the hash of the current block, the seedHash, and the boundary condition to be met ("target") */
  def ethGetWork: Either[Error, Response]

  /** Returns asynchronously the hash of the current block, the seedHash, and the boundary condition to be met ("target") */
  def asyncEthGetWork: AsyncResponse

  /** Used for submitting a proof-of-work solution */
  def ethSubmitWork(nonce: String, powHash: String, mixDigest: String): Either[Error, Response]

  /** Used for submitting asynchronously a proof-of-work solution */
  def asyncEthSubmitWork(nonce: String, powHash: String, mixDigest: String): AsyncResponse

  /** Used for submitting mining hashrate */
  def ethSubmitHashrate(hashrate: String, clientId: String): Either[Error, Response]

  /** Used for submitting asynchronously mining hashrate */
  def asyncEthSubmitHashrate(hashrate: String, clientId: String): AsyncResponse

  /** Returns the Whisper version this node offers */
  def shhVersion: Either[Error, Response]

  /** Returns asynchronously the Whisper version this node offers */
  def asyncShhVersion: AsyncResponse

  /** Returns the Whisper statistics for diagnostics */
  def shhInfo: Either[Error, Response]

  /** Returns asynchronously the Whisper statistics for diagnostics */
  def asyncShhInfo: AsyncResponse

  /** Generates a random symmetric key and stores it under id, which is then returned */
  def shhNewSymKey: Either[Error, Response]

  /** Generates asynchronously a random symmetric key and stores it under id, which is then returned */
  def asyncShhNewSymKey: AsyncResponse

  /** Creates and registers a new message filter to watch for inbound whisper messages */
  def shhNewMessageFilter(obj: ShhNewMessageFilterObject): Either[Error, Response]

  /** Creates asynchronously and registers a new message filter to watch for inbound whisper messages */
  def asyncShhNewMessageFilter(obj: ShhNewMessageFilterObject): AsyncResponse

  /** Creates a whisper message and injects it into the network for distribution */
  def shhPost(obj: ShhMessageObject): Either[Error, Response]

  /** Creates asynchronously a whisper message and injects it into the network for distribution */
  def asyncShhPost(obj: ShhMessageObject): AsyncResponse


}