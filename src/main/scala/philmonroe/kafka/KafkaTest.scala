package philmonroe.kafka

import org.apache.log4j.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._


object KafkaTest extends App {
  val LOG = Logger.getLogger(this.getClass)
  // Force consumption from beginning
  //  ZkUtils.maybeDeletePath(KafkaProperties.zkConnect, "/consumers/" + KafkaProperties.groupId)

  val producer = new Producer
  val consumer = new Consumer(KafkaProperties.topic)(msg =>
    LOG.info(s"fetched:  ${msg.partition} ${msg.offset} ${msg.key} ${msg.message}")
  )


  // Continuously push to kafka
  future {
    while (true) {
      Thread.sleep(100)
      producer.send(KafkaProperties.topic, s"time: ${System.currentTimeMillis()}")
    }
  }


  // Shutdown Gracefully
  sys.addShutdownHook {
    consumer.stop()
    producer.stop()
  }
}
