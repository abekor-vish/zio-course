package com.rockthejvm.sttpcourse
import sttp.client3._
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.{Schedule, Task, ZIO, ZIOAppDefault, durationInt}
import zio.Scope
import zio.ZIOAppArgs

// object RetryZio extends ZIOAppDefault {
//     override def run: ZIO[ZIOAppArgs & Scope, Throwable, Response[String]] = {
//         HttpClientZioBackend().flatMap
//     }
  
// }
