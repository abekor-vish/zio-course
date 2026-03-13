package com.rockthejvm.ziocourse

import zio.* 
import zio.Console.*


object RefExample001 extends ZIOAppDefault {

    def printRef(i: Int) = 
        printLine(s"value = $i")


    val equation = for {
        ref <- Ref.make(0) // Ref[Int]
        a   <- ref.get
        _   <- printRef(a)

        _   <- ref.set(1)
        b   <- ref.get
        _   <- printRef(b)

        _   <- ref.update(_+5)
        c   <- ref.get
        _   <- printRef(c)

        d   <- ref.modify(i => (i + 100, i + 10))
        e   <- ref.get
        _   <- printRef(d)
        _   <- printRef(e)
    } yield()

    val run = equation
}
// 7097273779