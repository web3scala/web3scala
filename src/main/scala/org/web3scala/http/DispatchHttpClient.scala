package org.web3scala.http

import dispatch._
import Defaults._
import org.json4s.JValue

class DispatchHttpClient(host: String = "127.0.0.1", port: Int = 8545, secure: Boolean = false) extends JValueHttpClient {
  override def async(request: Array[Byte]): Future[JValue] = {
    val req = DispatchHttpClient.httpRequest(host, port, secure, request)
    DispatchHttpClient.http(req OK as.json4s.Json)
  }
  override def sync(request: Array[Byte]): JValue = {
    async(request).apply()
  }
}
object DispatchHttpClient {
  private val httpInstance: Http = Http.default
  private def http: Http = httpInstance
  private def httpRequest(host: String, port: Int, secure: Boolean, request: Array[Byte]): Req = {
    val req = dispatch.host(host, port)
      .setBody(request)
      .setHeader("Content-Type", "application/json")
    if (secure) req.secure else req
  }
}