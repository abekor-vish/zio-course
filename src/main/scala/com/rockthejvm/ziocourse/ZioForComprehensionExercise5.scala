package com.rockthejvm.ziocourse

import zio.{ZIOAppDefault, Console}
import zio._

object ZioForComprehensionExercise5 extends ZIOAppDefault{
    val result = ZIO.succeed(List(1, 2, 3)).flatMap(nums =>
                        ZIO.succeed(nums.filter(_ % 2 != 0)).map(odds => odds.sum)
                    )

    val run = for {
            nums <- ZIO.succeed(List(1,2,3))
            odds <- ZIO.succeed(nums.filter(_ % 2 != 0))
            _    <- Console.printLine(s"Sum of odds: $odds")
        } yield odds.sum

}
