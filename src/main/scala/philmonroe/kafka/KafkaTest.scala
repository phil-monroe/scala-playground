package philmonroe.kafka

import org.apache.log4j.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.util.{Failure, Success}


object KafkaTest extends App {
  val LOG = Logger.getLogger(this.getClass)
  // Force consumption from beginning
  // ZkUtils.maybeDeletePath(KafkaProperties.zkConnect, "/consumers/" + KafkaProperties.groupId)

  val producer = new Producer
  val consumer = new Consumer(KafkaProperties.topic)


  // Continuously push to kafka
  future {
    while (true) {
      Thread.sleep(1000)
      val msg = s"time: ${System.currentTimeMillis()}"
      LOG.info(s"Sending: $msg")
      producer.send(KafkaProperties.topic, msg)
    }
  }


  // Continuously fetch from kafka
  future {
    while (true) {
      consumer.fetch() match {
        case Success(msg) =>
          if (msg != null)
            LOG.info(s">>> fetched:  ${msg.partition} ${msg.offset} ${msg.key} ${msg.message}")
          else
            LOG.error(s">>> fetched null?")

        case Failure(e) =>
          LOG.warn(s">>> failed fetching: $e")
          Thread.sleep(10000)
      }
    }
  }


  // Shutdown Gracefully
  sys.addShutdownHook {
    consumer.stop()
    producer.stop()
  }
}
