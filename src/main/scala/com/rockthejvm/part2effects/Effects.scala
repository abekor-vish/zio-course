package com.rockthejvm.part2effects

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.io.StdIn

object Effects extends App {

  // functional programming
  // Expressions!!
  def combine(a: Int, b: Int): Int = a + b

  
  
  // local reasoning = type signature describes the kind of computation that will be performed
  // referential transparency = ability to replace an expression with the value that it evaluates to
  val five = combine(3,5)
  val five_v2 = 3+5
  val five_v3 = 8
  // --------------------------------------------------------
  // not all expressions are referenctial transparency
  // example 1: printing
  val resultOfPrinting: Unit = println("Learning ZIO")
  val resultOfPrinting_v2: Unit = () // not the same

  // exampple 2: chaning a variable
  var anInt = 0
  val changingInt: Unit = (anInt = 3) // side effect
  val changingInt_v2: Unit = () // not the same


  // ---------------------------------------------------
  
  // side effects are inevitable.
  /* 
    Effect desires
    - the type signature should describe the KIND effect of the expression
    - the type signature describes the type of VALUE that it will produce
    - if side effect are required, construction must be separate from the Execution
   */
  
  /* 
    Example: Options = possibly absent values
    - type signature describes the kind of computation = a possibly absent value
    - type signature says that the computation returns an A, if the computation does produce something
    - no side effects are needed

    => Option is an effect
   */

  val anOption: Option[Int] = Option(42)

  /* 
    Example 2: Future
    - describes an asynchronous computation
    - produces a value of type A, if it finishes and it's successful
    - side effects are required, construction is NOT SEPERATE from execution

    => Future is not an effect
   */

  import scala.concurrent.ExecutionContext.Implicits.global
  val aFuture: Future[Int] = Future(42)

  /* 
    Example 3: = MyIO
    - describes  computation that might perform side effects
    - produces a value of type A, if it finishes and it's successful
    - side effects are required, construction is SEPERATE from execution

   */

  case class MyIO[A](unsafeRun: () => A) {
    def map[B](f: A => B): MyIO[B] = MyIO(() => f(unsafeRun()))
    def flatMap[B](f: A => MyIO[B]): MyIO[B] = MyIO(() => f(unsafeRun()).unsafeRun())
  }

  val anIOWithSideEffects: MyIO[Int] = MyIO(() => {
    println("Producing effects")
    42
  })



  
  println(changingInt)


  /* 
    takeaways:
      pure functional program = a big expression computing a value
      - referential transparency = can replace an expression with its value without changing behavior

      expressions performing side effects are not replaceable
      - i.e. break referential transparency
  
   */

  



  // -----------------------------------------------------

  /* 
    exercises - create some IO which:
      1. neasures the current time of the system
      2. measures thhe duration of a computation
          - use exercise 1
          use map/flatmap combination of MyIO
      3. reads something from tyhe console
      4. print something to the console( " name"), then read, then print welcome message with name
   */





  //1:
   val currentTime: MyIO[Long] = MyIO(() => System.currentTimeMillis())

  //2:
    def measure[A](computation: MyIO[A]): MyIO[(Long, A)] = for {
      start <- currentTime // 1.get the current time
      result <- computation //2. run the computation
      end <- currentTime // 3. get the current time again
    } yield(end - start, result) // 4. return (duration, result)



    def demoMeasurment(): Unit = {
      val computation = MyIO(() => {
        println("Crunching numbers...")
        Thread.sleep(1000)
        println("Done!")
        42
      })
    }


    // println(measure(computation).unsafeRun())

  //3: 
    val readLine: MyIO[String] = MyIO(() => StdIn.readLine())
    def greet(line: String): MyIO[Unit] = MyIO(() => println(line))
  //4:
    val program = for {
      _ <- greet("What is your name?")
      name <- readLine
      _ <- greet(s"Welcome, $name!")
    } yield()

    /* 
      println(What is your name?)
      name = readLine()
      println(hello, $name)
    
     */




    program.unsafeRun()

}



/* 

             Rule	                                                Real-world analogy
Type describes the kind of computation	          A labeled envelope: "URGENT", "FRAGILE", "MAY BE EMPTY"
Type describes the value produced	                The envelope says what's inside: "contains a check"
Construction is separate from execution	          Writing a to-do list vs. actually doing the tasks 

 */
