package com.rockthejvm.ziocourse

import zio.*
import zio.Console.*



object ZioArgsTest extends ZIOAppDefault {

    val failWithMsgEffect: ZIO[Any, Throwable, Unit] = 
        // printLineError("Usage: Yoda ...").flatMap({
        //     _ => ZIO.fail(Exception("Usage Exception"))
        // })
        printLineError("Usage: Yoda ...") *> ZIO.fail(Exception("Usage Exception")) // *> means 'and then'
    
    // Idiomatic (using flatmap)

    val blueprint: ZIO[ZIOAppArgs, Throwable, Chunk[String]] = for {
        args <- ZIOAppArgs.getArgs
        _    <- ZIO.when(args.isEmpty)(failWithMsgEffect)
    }yield args

    val run: ZIO[ZIOAppArgs, java.io.IOException, Unit] = blueprint.foldZIO(
        failure => ZIO.unit,
        args => ZIO.foreach(args) {
            arg => printLine(arg)
        }.unit
    )
}
