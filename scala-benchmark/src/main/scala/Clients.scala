
import javax.ws.rs.client.ClientBuilder

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.ConnectionKeepAliveStrategy
import org.apache.http.impl.client.{CloseableHttpClient, DefaultHttpClient, HttpClients}
import org.apache.http.impl.conn.{PoolingClientConnectionManager, PoolingHttpClientConnectionManager}
import org.apache.http.protocol.HttpContext

import scala.io.Source

object Clients {
  lazy val httpClient = {
    val cm = new PoolingClientConnectionManager()
    cm.setMaxTotal(20)
    cm.setDefaultMaxPerRoute(20)

    val keepalive = new ConnectionKeepAliveStrategy {
      override def getKeepAliveDuration(response: HttpResponse, context: HttpContext): Long = 30 * 1000
    }

    val client = new DefaultHttpClient(cm)
    client.setKeepAliveStrategy(keepalive)
    client
  }

  lazy val newHttpClient = {
    val cm = new PoolingHttpClientConnectionManager()
    cm.setMaxTotal(10)
    cm.setDefaultMaxPerRoute(10)

    HttpClients.createMinimal(cm)
  }

  val get = new HttpGet("http://localhost:8080/ping")
  get.addHeader("User-Agent", "Phil")

  def makeRequest(client: CloseableHttpClient) = {
    val res = client.execute(get)
    Source.fromInputStream(res.getEntity.getContent).mkString
    get.releaseConnection()
    res.close()
  }

  def makeHttpClientReques = makeRequest(httpClient)

  def makeNewHttpClientReques = makeRequest(newHttpClient)

  lazy val jerseyClient = ClientBuilder.newClient()
  lazy val resource = jerseyClient.target("http://localhost:8080").path("ping")

  def makeJerseyRequest() = {
    val res = resource.request().get(classOf[String])
  }
}
