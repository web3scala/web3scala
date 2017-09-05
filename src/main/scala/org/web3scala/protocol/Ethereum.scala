package org.web3scala.protocol

import org.web3scala.model.{AsyncResponse, Block, Response}

trait Ethereum {

  /** Returns current client version */
  def web3ClientVersion: Response

  /** Returns asynchronously current client version */
  def asyncWeb3ClientVersion: AsyncResponse

  /** Returns Keccak-256 (not the standardized SHA3-256) of the given data */
  def web3Sha3(data: String): Response

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
  def netVersion: Response

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
  def netListening: Response

  /** Returns asynchronously true if client is actively listening for network connections */
  def asyncNetListening: AsyncResponse

  /** Returns number of peers currently connected to the client */
  def netPeerCount: Response

  /** Returns asynchronously number of peers currently connected to the client */
  def asyncNetPeerCount: AsyncResponse

  /** Returns the current ethereum protocol version */
  def ethProtocolVersion: Response

  /** Returns asynchronously the current ethereum protocol version */
  def asyncEthProtocolVersion: AsyncResponse

  /** Returns an object with data about the sync status or false */
  def ethSyncing: Response

  /** Returns asynchronously an object with data about the sync status or false */
  def asyncEthSyncing: AsyncResponse

  /** Returns the client coinbase address */
  def ethCoinbase: Response

  /** Returns asynchronously the client coinbase address */
  def asyncEthCoinbase: AsyncResponse

  /** Returns true if client is actively mining new blocks */
  def ethMining: Response

  /** Returns asynchronously true if client is actively mining new blocks */
  def asyncEthMining: AsyncResponse

  /** Returns the number of hashes per second that the node is mining with */
  def ethHashrate: Response

  /** Returns asynchronously the number of hashes per second that the node is mining with */
  def asyncEthHashrate: AsyncResponse

  /** Returns the current price per gas in wei */
  def ethGasPrice: Response

  /** Returns asynchronously the current price per gas in wei */
  def asyncEthGasPrice: AsyncResponse





  /** Returns a list of addresses owned by client */
  def ethAccounts: Response

  /** Returns the number of most recent block */
  def ethBlockNumber: Response

  /** Returns the balance of the account of given address */
  def ethGetBalance(address: String, defaultBlock: Block): Response

  /** Returns the value from a storage position at a given address */
  def ethGetStorageAt(address: String, position: String, defaultBlock: Block): Response

  /** Returns the number of transactions sent from an address */
  def ethGetTransactionCount(address: String, defaultBlock: Block): Response

  /** Returns the number of transactions in a block from a block matching the given block hash */
  def ethGetBlockTransactionCountByHash(blockHash: String): Response

  /** Returns the number of transactions in a block from a block matching the given block number */
  def ethGetBlockTransactionCountByNumber(defaultBlock: Block): Response

  /** Returns the number of uncles in a block from a block matching the given block hash */
  def ethGetUncleCountByBlockHash(blockHash: String): Response

  /** Returns the number of uncles in a block from a block matching the given block number */
  def ethGetUncleCountByBlockNumber(defaultBlock: Block): Response

  /** Returns code at a given address */
  def ethGetCode(address: String, defaultBlock: Block): Response

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
  def ethSign(address: String, message: String): Response

}