package com.rockthejvm.sttpcourse

import zio.ZIOAppDefault
import sttp.capabilities.WebSockets
import sttp.ws.WebSocket
import sttp.client3._
import zio.{Console, _}
import sttp.client3.httpclient.zio.HttpClientZioBackend


object WebSocketZio extends ZIOAppDefault {
    def useWebSocket(ws: WebSocket[Task]): Task[Unit] = {
        def send(i: Int) = ws.sendText(s"Hello $i!")
        for {
            _  <- send(1)
            _  <- send(2)
            t1 <- ws.receiveText()
            _  <- Console.printLine(s"RECIEVED: $t1")
            t2 <- ws.receiveText()
            _  <- Console.printLine(s"RECIEVED: $t2")

        } yield()
    }

    def sendAndPrint(backend: SttpBackend[Task, WebSockets]): Task[Response[Unit]] = 
        backend.send(basicRequest.get(uri"wss://ws.postman-echo.com/raw").response(asWebSocketAlways(useWebSocket)))
  
    override def run: ZIO[ZIOAppArgs & Scope, Any, Any] = HttpClientZioBackend.scoped().flatMap(sendAndPrint)
}
