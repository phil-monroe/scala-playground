package philmonroe.curator

import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.{RetryNTimes, RetryOneTime, ExponentialBackoffRetry}

import scala.collection.JavaConversions._
import scala.util.Try

object MultiClusterTest extends App {


  def createClient(connect: String, jaasConf: String = "jaas.conf") = {
    System.setProperty("java.security.auth.login.config", jaasConf)
    val retryPolicy = new RetryNTimes(0, 1000)
    val client = CuratorFrameworkFactory.newClient(connect, retryPolicy)
    client.start()
    client.blockUntilConnected()

    client
  }

  def printNodes(client: CuratorFramework): Unit = {
    Try(println(client.getChildren.forPath("/").toSeq)).recover{
      case e =>
        println("cannont fetch nodes", e.getMessage)
        println(e.getStackTrace.mkString("\n! "))
    }
  }

  val client = createClient("localhost:2181", "jaas.conf")
  val client2 = createClient("localhost:2182", "jaas.conf")


  println(">>>> Client 1")
  printNodes(client)

  println(">>>> Client 2")
  printNodes(client2)

  client.close()
  client2.close()
}
