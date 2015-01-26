package philmonroe.dropwizard

import javax.ws.rs.{GET, Path}

import com.massrelevance.dropwizard.ScalaApplication
import com.massrelevance.dropwizard.bundles.ScalaBundle
import io.dropwizard.Configuration
import io.dropwizard.setup.{Environment, Bootstrap}

import scala.util.Random


@Path("/ping")
object PingResource {

  @GET
  def ping() = {
//    Thread.sleep(100)
    "pong"
  }
}

object Server extends ScalaApplication[Configuration] {
  override def initialize(boot: Bootstrap[Configuration]): Unit = {
    boot.addBundle(new ScalaBundle)
  }

  override def run(conf: Configuration, env: Environment): Unit = {
    env.jersey().register(PingResource)
  }
}
