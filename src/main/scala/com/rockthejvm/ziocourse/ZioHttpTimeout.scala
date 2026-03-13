package com.rockthejvm.ziocourse

import zio.*
import zio.Console.* 
import scala.io.Source
import java.io.IOException
import java.util.concurrent.TimeoutException
import com.rockthejvm.part2effects.ZIOErrorHandling.option2ZIO


object ZioHttpTimeout extends ZIOAppDefault {
    
    // val blueprint: ZIO[Any, Throwable, String] = 
    //     val url = "https://httpbin.org/get"
    //     ZIO.attempt {
    //         Source.fromURL(url).mkString
    //     }.timeoutFail(TimeoutException("request timed out"))(2000.millisecond)

    // // ----------

    // // [1] show this first, and the problem it has, i.e. that
    // // timeout doesnt make this an error, it still success, but its value is `None`

    // val run = blueprint.foldZIO(
    //     failure => printLineError(s"Failure: $failure"),
    //     success => printLine(s"Success: $success")
    // )

    val url = "https://httpbin.org/get"

    def getUrlContent(url: String, timeoutInMs: Int): ZIO[Any, Throwable, String] = 
        ZIO.attempt {
            Source.fromURL(url).mkString
        }.timeout(2000.millisecond).flatMap {
            maybeString => convertOptionToZIO(maybeString, TimeoutException("Operation Timed out"))
        }

    def convertOptionToZIO[A, E](option: Option[A], error: => E): ZIO[Any, E, A] =
        ZIO.fromOption(option).orElseFail(error)

    val blueprint = for {
        result <- getUrlContent(url, 10_000)
    } yield result

    val run = blueprint.foldZIO(
        failure => failure match {
            case e1: java.net.URISyntaxException => printLineError("URI error")
            case e2: java.net.UnknownHostException => printLineError("Uknown to host")
            case e3: java.util.concurrent.TimeoutException => printLineError(("Timeout Error"))
        },
        success => printLine(s"SUCCESS = $success")
    )
}
