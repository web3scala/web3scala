package org.web3scala.http

import dispatch.Future
import org.json4s.JValue

trait HttpClient
trait JValueHttpClient extends HttpClient {
  def async(request: Array[Byte]): Future[JValue]
  def sync(request: Array[Byte]): JValue
}
trait StringHttpClient extends HttpClient {
  def async(request: Array[Byte]): Future[String]
  def sync(request: Array[Byte]): String
}
