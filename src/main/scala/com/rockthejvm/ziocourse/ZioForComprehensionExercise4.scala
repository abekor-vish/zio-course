package com.rockthejvm.ziocourse

import zio.{ZIOAppDefault, Console}
import zio._

object ZioForComprehensionExercise4 extends ZIOAppDefault{
    def fetchUser(id: Int): Task[String] = ZIO.succeed(s"User$id")
    def fetchScore(user: String): Task[Int] = ZIO.succeed(user.length)

    val result = fetchUser(1).flatMap(user =>
                    fetchScore(user).flatMap(score =>
                                        Console.printLine(s"$user scored $score").map(_ => score)
                                        )
                                    )

    val run = for {
        user  <- fetchUser(12)
        score <- fetchScore(user)
        _     <- Console.printLine(s"$user scored $score")
    } yield score

}
