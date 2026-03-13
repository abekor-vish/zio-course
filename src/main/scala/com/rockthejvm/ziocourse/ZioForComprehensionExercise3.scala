package com.rockthejvm.ziocourse

import zio.{ZIOAppDefault, Console}
import zio._

object ZioForComprehensionExercise3 extends ZIOAppDefault{
    val result = ZIO.succeed(10).flatMap(a =>
            ZIO.succeed(20).flatMap(b =>
                Console.printLine(s"Sum is ${a + b}").map(_ => a + b)
                )
            )

    val run = for {
        a   <- ZIO.succeed(10)
        b   <- ZIO.succeed(20)
        _   <- Console.printLine(s"Sum is ${a + b}")
    } yield a + b
}
