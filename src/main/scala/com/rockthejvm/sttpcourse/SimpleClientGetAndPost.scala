package com.rockthejvm.sttpcourse

import sttp.client3._
import java.util.UUID

object SimpleClientGetAndPost extends App {
    val client = SimpleHttpClient()

    try {
        val response: Response[Either[String,String]] = client.send(basicRequest.get(uri"https://httpbin.org/get"))
        // println(response.body)
        response.body match
            case Left(value) => println(s"Non-2xx response to Get with code ${response.code} :\n$value")
            case Right(value) => println(s"2xx response to GET: \n$value")
        
        println("----\n")

        val request2: Request[String, Any] = basicRequest
            .header("X-Correlation-ID", UUID.randomUUID().toString)
            .response(asStringAlways)
            .body("Hello, World!")
            .post(uri"https://httpbin.org/post")
        val response2: Response[String] = client.send(request2)

        println(s"Response to POST: \n${response2.body}")
        
    } finally client.close()

    // val request2: Request[]


  
}
