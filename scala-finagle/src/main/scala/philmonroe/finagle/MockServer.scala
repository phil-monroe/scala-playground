package philmonroe.finagle

import com.twitter.finagle.{Http, ListeningServer, Service}
import com.twitter.util.{Await, Future}
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil.UTF_8

import scala.util.Try

object MockServer {
  var server: Option[ListeningServer] = None
  var _log = collection.mutable.Seq.empty[(HttpRequest, HttpResponse)]

  def log = _log.toSeq

  def clear: Unit = _log = collection.mutable.Seq.empty[(HttpRequest, HttpResponse)]


  def start(fn: (String, HttpHeaders) => String): Unit = {
    val service = new Service[HttpRequest, HttpResponse] {
      override def apply(request: HttpRequest): Future[HttpResponse] = {
        val resp = Try(fn(request.getUri, request.headers()))
        println(resp)
        Future.value(resp.map(OK).getOrElse(OK("Nope")))
      }
    }
    server = Some(Http.serve(":8080", service))

  }

  def stop() = {
    server.map(_.close())
    server = None
  }


  private def OK(body: String): HttpResponse = {
    respond(HttpResponseStatus.OK, body)
  }

  private def respond(status: HttpResponseStatus, body: String): HttpResponse = {
    val response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status)
    response.setHeader("Content-Type", "application/json")
    response.setHeader("Cache-Control", "no-cache")
    response.setContent(copiedBuffer(body, UTF_8))
    response
  }
}

object MockServerTest extends App {

  MockServer.start {
    case ("/foo", headers) =>
      "bar"
  }

  Await.ready(MockServer.server.get)
}
