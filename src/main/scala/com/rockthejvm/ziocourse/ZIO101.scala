
package com.rockthejvm.ziocourse
import zio.{ZIOAppDefault, Console}
import zio.*
import scala.io.StdIn

object ZIO101 extends ZIOAppDefault {


    // Task[String] == ZIO[Any, Throwable, String]
    val promptForName: Task[String] = 
        ZIO.attempt(StdIn.readLine("What is your name? ")) // ZIO.attempt is used for when we know it can fail


    // UIO[Unit] == ZIO[Any, Nothing, Unit] which represents an Unexceptional effect
    def printName(name: String): UIO[Unit] = 
        ZIO.succeed(println(s"Hello $name")) // ZIO.succeed has not error type so its Nothing because its a successful effect

    /*
      when you have a sequenctial order, think of "And Then do this" or "Then do this". So
      think of flatMap as "andThen" or "then" in other languages, it takes the result of the first effect and feeds it into the second effect. 
      So we are taking the name that we got from the promptForName effect and passing it to the printName effect.
      */

    val run = 
        for {
            name <- promptForName  // "run this side effect, and then call the result 'name'"
            _ <- printName(name)  // "run this effect (using 'name' from above), and I dont need the result so I will call it _"
        }yield()
    /* 
        The Rule of Thumb
        Every line in a for comprehension follows the same pattern:

        result <- doSomething

        - Right side (doSomething): must be a ZIO effect
        - Left side (result): the unwrapped success value, available to all lines below
        - _: means "run it but I don't need the result"
        - yield: what the whole for block produces at the end
     */

    // ---------------------------------------------


    /* 
    
    val run = 
    for {
        name <- promptForName  // "run this side effect, and then call the result 'name'"
        _ <- printName(name)  // "run this effect (using 'name' from above), and I dont need the result so I will call it _"
    }yield()
    
    These are both equivalent

    val run = for {
        _ <- Console.print("What is your name son: ")
        name <- Console.readLine
        _ <- Console.print(s"Hello $name")
    } yield()
     */
}