import java.util.concurrent.{Callable, TimeUnit}

import com.codahale.metrics.{ConsoleReporter, MetricRegistry}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import concurrent._
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Test extends App {

  val metrics = new MetricRegistry
  metrics.timer("httpclient")

  val reporter = ConsoleReporter.forRegistry(metrics)
    .convertRatesTo(TimeUnit.MILLISECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .build()
  reporter.start(10, TimeUnit.SECONDS)

  val om = new ObjectMapper
  om.registerModule(DefaultScalaModule)


  val makeRequest = new Callable[Unit] {
    override def call(): Unit = {
      //      Clients.makeJerseyRequest
      //      Clients.makeHttpClientReques
      Clients.makeNewHttpClientReques
    }
  }

  0.until(1000).map { _ => makeRequest.call()}

  val futs = 0.until(4).map { _ =>
    future {
      while (true) {
        metrics.timer("httpclient").time(makeRequest)
      }
    }
  }

  futs.map(Await.ready(_, Duration.Inf))
}

