package com.rockthejvm.part2effects

import zio.*

import scala.io.StdIn

object ZIOEffects{

    // ZIO[R,E,A] => (Environment 'What effects needs to run', Error 'how the effect can fail', Success 'what the effect produces on success')
    // success
    val meaningOfLife: ZIO[Any, Nothing, Int] = ZIO.succeed(42)
    // failure
    val aFailure: ZIO[Any, String, Nothing] = ZIO.fail("Something went wrong")
    // suspension/delay
    val aSuspendedZIO: ZIO[Any, Throwable, Int] = ZIO.suspend(meaningOfLife)


    // map + flatMap
    val improvedMOL = meaningOfLife.map(_ * 2)
    val printinMOL = meaningOfLife.flatMap(mol => ZIO.succeed(println(mol)))
    
    // for comprehension
    val smallProgram = for {
        _ <- ZIO.succeed(println("What is your name?"))
        name <- ZIO.succeed(StdIn.readLine())
        _ <- ZIO.succeed(println(s"Hello $name!, welcome to the ZIO Course"))
    } yield ()


    // A LOT of combinators
    // zip, zipWith, zipWithPar, zipWithParN, zipWithN, ...
    val anotherMOL = ZIO.succeed(100)
    val tupledZIO = meaningOfLife.zip(anotherMOL)
    val combinedZIO = meaningOfLife.zipWith(anotherMOL)(_ * _)

    /**
      * Type aliases of ZIOs
      */

    // UIO = ZIO[Any, Nothing, A] => no requirements, no failure, produced A
    val aUIO: UIO[Int] = ZIO.succeed(99)
    // URIO[R, A] = ZIO[R, Nothing, A] - cannot fail
    val aURIO: URIO[Int, Int] = ZIO.succeed(67)
    // RIO[R, A] = ZIO[R, Throwable, A] => can fail with a Throwable
    val aRIO: RIO[Int, Int] = ZIO.succeed(98)
    val aFailedRIO: RIO[Int, Int] = ZIO.fail(new RuntimeException("Boom!"))

    // Task[A] = ZIO[Any, Throwable, A] => no requirements, might fail with Throwable, produced A
    val aSuccessfulTask: Task[Int] = ZIO.succeed(89)
    val aFailedTask: Task[Int] = ZIO.fail(new RuntimeException("Something bad"))

    // IO[E,A] = ZIO[Any, E, A] => No requirements
    val aSuccessfulIO: IO[String, Int] = ZIO.succeed(34)
    val aFailedIO: IO[String, Int] = ZIO.fail("Something bad happened")

    def main(args: Array[String]): Unit = {
        println(meaningOfLife) // ZIO[Any, Nothing, Int] description of the effect
        println(aFailure) // ZIO[Any, String, Nothing] description of the effect
        println(aSuspendedZIO) // ZIO[Any, Throwable, Int] description of the effect
        println(improvedMOL)
        println(printinMOL)


        def run = smallProgram
    }

}
