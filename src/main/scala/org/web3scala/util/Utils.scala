package org.web3scala.util

import org.web3scala.exception.{QuantityDecodingException, QuantityEncodingException}

/**
  * Helper functions.
  *
  * @see https://github.com/ethereum/wiki/wiki/JSON-RPC#hex-value-encoding
  */
object Utils {
  private val HEX_PREFIX = "0x"
  @throws(classOf[QuantityEncodingException])
  def long2hex(value: Long): String =
    if (scala.math.signum(value) > 0)
      HEX_PREFIX + value.toHexString
    else
      throw new QuantityEncodingException("Negative value not allowed")
  @throws(classOf[QuantityEncodingException])
  def int2hex(value: Int): String =
    if (scala.math.signum(value) > 0)
      HEX_PREFIX + Integer.toHexString(value)
    else
      throw new QuantityEncodingException("Negative value not allowed")
  @throws(classOf[QuantityDecodingException])
  def hex2long(value: String): Long =
    if (isValidHex(value))
      java.lang.Long.parseLong(value.substring(2), 16)
    else
      throw new QuantityDecodingException("Invalid hex value")
  @throws(classOf[QuantityDecodingException])
  def hex2int(value: String): Int =
    if (isValidHex(value))
      java.lang.Integer.parseInt(value.substring(2), 16)
    else
      throw new QuantityDecodingException("Invalid hex value")
  def isValidHex(value: String): Boolean = {
    if (value == null) return false
    if (value.length < 3) return false
    if (value.charAt(0) != '0' || value.charAt(1) != 'x') return false
    if (value.length > 3 && value.charAt(2) == '0') return false
    true
  }
}