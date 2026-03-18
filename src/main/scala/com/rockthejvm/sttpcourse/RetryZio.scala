package com.rockthejvm.sttpcourse
import sttp.client3._
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.{Schedule, Task, ZIO, ZIOAppDefault, durationInt}
import zio.Scope
import zio.ZIOAppArgs

object RetryZio extends ZIOAppDefault {
    // override def run: ZIO[ZIOAppArgs & Scope, Throwable, Response[String]] = {
    //     HttpClientZioBackend().flatMap( backend =>{
    //         val localhostRequest = basicRequest
    //             .get(uri"http://localhost/test")
    //             .response(asStringAlways)
            
    //         val sendWithRetries: Task[Response[String]] = localhostRequest
    //             .send(backend)
    //             .either
    //             .repeat(
    //                 Schedule.spaced(1.second) *>
    //                     Schedule.recurs(10) *>
    //                     Schedule.recurWhile(result => RetryWhen.Default(localhostRequest, result))
    //             )
    //             .absolve

    //         sendWithRetries.ensuring(backend.close().ignore)     
    //     })
    // }

    override def run: ZIO[ZIOAppArgs & Scope, Throwable, Response[String]] = {
        val localhostRequest = basicRequest
            .get(uri"http://localhost/test")
            .response(asStringAlways)

        val retrySchedule = 
            Schedule.spaced(1.second) *>
                Schedule.recurs(10) *>
                Schedule.recurWhile[Either[Throwable, Response[String]]](result =>
                    RetryWhen.Default(localhostRequest, result))
        for {
            backend <- HttpClientZioBackend()
            result  <- localhostRequest
                            .send(backend)
                            .either
                            .repeat(retrySchedule)
                            .absolve
                            .ensuring(backend.close().ignore)
                            
        } yield result

    }
}