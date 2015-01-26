import java.util.concurrent.Executors
import javax.ws.rs.client.{Invocation, ClientBuilder}

import scala.util.Random
import scala.concurrent._
import scala.concurrent.duration._

object ServerBenchmark extends App {
  val N = 100000

  def time(func: => Any): Double = {
    val start = System.nanoTime()
    func
    val end = System.nanoTime()
    (end - start)/1000000000.0
  }

  implicit val ec = new ExecutionContext {
    val threadPool = Executors.newFixedThreadPool(50)

    def execute(runnable: Runnable) {
      threadPool.submit(runnable)
    }

    def reportFailure(t: Throwable) {}
  }

  val rand = new Random()
  val client = ClientBuilder.newClient()
  val localhost = client.target("http://localhost:8080")
  val paths = Array(localhost.path("/ping"))
  paths.head.request().get()

  val requests = (0 until N).map { i => paths(rand.nextInt(paths.length)).request() }


  val start = System.nanoTime()

  val times = requests.map { req =>
    future{
      time { req.get().close() }
    }
  }.map{ f =>
    Await.result(f, 1000 second)
    f.value.get.get
  }

  val end = System.nanoTime()
  val elapsed = (end - start)/1000000000.0


  val avgReq= times.sum.toFloat / N

  println(s"time:    $elapsed")
  println(s"req/sec: ${N/elapsed}")
  println(s"avg req: ${avgReq}")

}
