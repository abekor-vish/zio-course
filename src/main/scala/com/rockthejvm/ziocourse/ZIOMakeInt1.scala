package com.rockthejvm.ziocourse
import zio.*
import zio.Console.*

def makeInt(s: String): ZIO[Any, NumberFormatException, Int] = 
    ZIO.attempt(s.toInt)
        .refineToOrDie[NumberFormatException]

object ZioRealWorldRun extends ZIOAppDefault:

    val app: ZIO[Any, NumberFormatException, Int] = for {
        a <- makeInt("1")
        b <- makeInt("9")
        c <- makeInt("3")
    } yield a + b + c

    val run: ZIO[Any, java.io.IOException, Unit] = app.foldZIO(
        failure => Console.printLineError(s"FAILURE = $failure"),
        success => Console.printLine(s"SUCCESS = $success")
    )

