package com.rockthejvm.sttpcourse

import sttp.client3._
import sttp.client3.upicklejson._
import upickle.default._

object SttpDemoV3 extends App {
    
    // val client = SimpleHttpClient()
    // val response = client.send(basicRequest.get(uri"https://httpbin.org/get"))

    // println(response.body)

    val client = SimpleHttpClient()
    case class MyRequest(field1: String, field2: Int)
    case class HttpBinResponse(origin: String, headers: Map[String, String])

    implicit val myRequestRW: ReadWriter[MyRequest] = macroRW[MyRequest]
    implicit val responseRW: ReadWriter[HttpBinResponse] = macroRW[HttpBinResponse]

    val request = basicRequest
        .post(uri"https://httpbin.org/post")
        .body(MyRequest("test", 42))
        .response(asJson[HttpBinResponse])

    val response = client.send(request)

    response.body match {
        case Left(value) => println(s"Got response expection: \n $value")
        case Right(value) => println(s"Origin's ip: ${value.origin}, header count: ${value.headers.size}")
    }

}
