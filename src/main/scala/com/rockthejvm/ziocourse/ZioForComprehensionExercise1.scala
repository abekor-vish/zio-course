package com.rockthejvm.ziocourse

import zio.{ZIOAppDefault, Console}
import zio._

object ZioForComprehensionExercise1 extends ZIOAppDefault{
    // Exercise 1.
    val example = ZIO.succeed(42).flatMap(n => ZIO.succeed(n * 2))

    // val run = example

    /**
        The pattern to follow is simple — every flatMap(x => ...) becomes x <- ...:

        Step 1: What's before the .flatMap? That's your first <-:
            n <- ZIO.succeed(42)
        
        Step 2: What's inside the flatMap? Thats the next line. Since it's the last thing and produces a value, it becomes yield:
            yield ZIO.succeed(n * 2)  // ❌ wrong — don't wrap in ZIO again
            yield n * 2               // ✅ correct

    */

    val run = for {
        n <- ZIO.succeed(42)

    }yield n * 2

}
