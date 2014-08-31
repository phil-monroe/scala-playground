package philmonroe.kafka

import org.apache.log4j.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._


object KafkaTest extends App {
  val LOG = Logger.getLogger(this.getClass)
  // Force consumption from beginning
  //  ZkUtils.maybeDeletePath(KafkaProperties.zkConnect, "/consumers/" + KafkaProperties.groupId)

  var run = true

  val producer = new Producer
  val consumer = new Consumer(KafkaProperties.topic)(msg =>
    LOG.info(s"fetched:  ${msg.partition} ${msg.offset} ${msg.key} ${msg.message}")
  )


  // Continuously push to kafka
  (0 to 10).foreach { i =>
    future {
      while (run)
        producer.send(KafkaProperties.topic, s"time: ${System.currentTimeMillis()}")
    }
  }


  // Shutdown Gracefully
  sys.addShutdownHook {
    run = false
    consumer.stop()
    producer.stop()
  }
}
