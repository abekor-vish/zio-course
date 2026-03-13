package com.rockthejvm.ziocourse

import zio.{ZIOAppDefault, Console}
import zio._

object ZioForComprehensionExercise2 extends ZIOAppDefault{
    val result = ZIO.succeed("Alice").flatMap(name =>
                    Console.printLine(s"Hello $name!").flatMap(_ =>
                        ZIO.succeed(name.length)
                        )
                    )

    /* 
        Apply the rule to each flatMap one at a time:
        flatMap(name => ...) → name <- ZIO.succeed("Alice")
        flatMap(_ => ...) → _ <- Console.printLine(s"Hello $name!")
        Last value is ZIO.succeed(name.length) → yield name.length (unwrap the ZIO.succeed)
     */

    val run = for {
        name <- ZIO.succeed(("Alice"))
        _    <- Console.printLine(s"Hello $name")
    } yield name.length
}
