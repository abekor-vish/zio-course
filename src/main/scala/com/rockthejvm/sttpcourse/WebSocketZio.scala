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
    // override def run: ZIO[ZIOAppArgs & Scope, Any, Any] = for {
    //     backend  <- HttpClientZioBackend.scoped()
    //     response <- sendAndPrint(backend)
    // } yield response


    // val result = ZIO.succeed(42).flatMap(n => ZIO.succeed(n * 2))
    // // for {
    // //     _ <- ZIO.succeed(n * 2)
    // // } yield ()
    // val result2 = ZIO.succeed("Alice").flatMap(name =>
    //                 Console.printLine(s"Hello $name!").flatMap(_ =>
    //                     ZIO.succeed(name.length)
    //                     )
    //                 )
    
    // val result3 = ZIO.succeed(10).flatMap(a =>
    //                 ZIO.succeed(20).flatMap(b =>
    //                     Console.printLine(s"Sum is ${a + b}").map(_ => a + b)
    //                     )
    //                 )

    
    // def fetchUser(id: Int): Task[String] = ZIO.succeed(s"User$id")
    // def fetchScore(user: String): Task[Int] = ZIO.succeed(user.length)

    //     val result4 = fetchUser(1).flatMap(user =>
    //         fetchScore(user).flatMap(score =>
    //             Console.printLine(s"$user scored $score").map(_ => score)
    //         )
    // )



}
