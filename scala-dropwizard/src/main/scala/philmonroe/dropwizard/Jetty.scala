package philmonroe.dropwizard

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

import org.eclipse.jetty.server.{Request, Server}
import org.eclipse.jetty.server.handler.AbstractHandler

import scala.util.Random


object HelloHandler extends AbstractHandler{
  override def handle(p1: String, baseRequest: Request, p3: HttpServletRequest, response: HttpServletResponse): Unit = {
    Thread.sleep(100)

    response.setContentType("text/html;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);
    baseRequest.setHandled(true);
    response.getWriter().println("<h1>Hello World</h1>");
  }
}


object Jetty extends App {

  val server = new Server(8080)
  server.setHandler(HelloHandler)
  server.start()
  server.join()

}
