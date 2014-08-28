package philmonroe.kafka

import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.log4j.Logger

import scala.collection.mutable
import scala.util.Try

import scala.concurrent._
import ExecutionContext.Implicits.global

class Consumer(topic: String, topicThreads: Int = 1) {
  val LOG = Logger.getLogger(this.getClass)

  val consumer = kafka.consumer.Consumer.create(KafkaProperties.consumerConfig)
  val topicCountMap = Map(topic -> topicThreads)
  val decoder = new StringDecoder()
  val consumerMap = consumer.createMessageStreams(topicCountMap, decoder, decoder)
  val messageQueue = new mutable.Queue[MessageAndMetadata[String, String]]()

  consumerMap.get(topic).get.foreach { stream =>
    future {
      val iter = stream.iterator()
      while (true) {
        if (iter.hasNext())
          messageQueue.enqueue(iter.next())
      }
    } onComplete { res =>
      LOG.error("FINISHED PULLING FROM KAFKA")
    }
  }


  def stop() = consumer.shutdown()

  def fetch() = Try(messageQueue.dequeue())

}
