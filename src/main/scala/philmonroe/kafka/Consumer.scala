package philmonroe.kafka

import java.util.concurrent.Executors

import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.log4j.Logger

import scala.concurrent._

class Consumer(topic: String, topicThreads: Int = 1)(callback: (MessageAndMetadata[String, String]) => Unit) {
  private val LOG = Logger.getLogger(this.getClass)

  implicit private val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(topicThreads))

  private val consumer = kafka.consumer.Consumer.create(KafkaProperties.consumerConfig)
  private val topicCountMap = Map(topic -> topicThreads)
  private val decoder = new StringDecoder()
  private val consumerMap = consumer.createMessageStreams(topicCountMap, decoder, decoder)

  consumerMap.get(topic).get.foreach { stream =>
    future {
      val iter = stream.iterator()
      while (true)
        if (iter.hasNext())
          callback(iter.next())

    } onComplete { res =>
      LOG.error("FINISHED PULLING FROM KAFKA")
    }
  }


  def stop() = consumer.shutdown()

}
