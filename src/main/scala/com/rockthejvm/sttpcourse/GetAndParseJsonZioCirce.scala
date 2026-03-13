package com.rockthejvm.sttpcourse

import io.circe.generic.auto._
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.httpclient.zio.{HttpClientZioBackend, send}
import zio._
import zio.Console.*
import com.rockthejvm.sttpcourse.SttpDemoV3.response


object GetAndParseJsonZioCirce extends ZIOAppDefault {

    override def run = {
        case class HttpBinResponse(origin: String, headers: Map[String, String])
        
        val request = basicRequest
            .get(uri"https://httpbin.org/get")
            .response(asJson[HttpBinResponse])

        for {
            response <- send(request)
            _        <- printLine(s"Got response code: ${response.code}")
            _        <- printLine(response.body.toString)
        } yield ()
    }.provideLayer(ZLayer.debug("additional layer")++ HttpClientZioBackend.layer())
  
}
