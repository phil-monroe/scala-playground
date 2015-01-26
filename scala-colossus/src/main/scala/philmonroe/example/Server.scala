package philmonroe.example

import colossus._
import service._
import protocols.http._
import UrlParsing._
import HttpMethod._

object Server extends App {

  implicit val io_system = IOSystem()

  Service.become[Http]("http-bench", 8080){
    case request @ Get on Root / "ping" =>
//      Thread.sleep(100)
      request.ok(s"pong - ${System.nanoTime()}")
    case request @ Get on _ =>
      request.ok("fallback")
  }
}

