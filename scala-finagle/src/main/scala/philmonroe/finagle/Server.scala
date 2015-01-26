package philmonroe.finagle

import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.future
import scala.util.Random

object Server extends App {
  val service = new Service[HttpRequest, HttpResponse] {
    def apply(req: HttpRequest): Future[HttpResponse] =
      Future.value {
//        Thread.sleep(100)

        new DefaultHttpResponse(req.getProtocolVersion, HttpResponseStatus.OK)
      }
  }

  val server = Http.serve(":8080", service)
  Await.ready(server)
}