package philmonroe.kafka

import java.util.Properties

import kafka.consumer.ConsumerConfig
import kafka.producer.ProducerConfig

object KafkaProperties {
   val zkConnect = "127.0.0.1:2181"
   val groupId = "group1"
   val topic = "topic1"
   val kafkaServerURL = "localhost"
   val kafkaServerPort = 9092
   val kafkaProducerBufferSize = 64 * 1024
   val connectionTimeOut = 100000
   val reconnectInterval = 10000

   def brokerList = s"$kafkaServerURL:$kafkaServerPort"

   def producerConfig = {
     val props = new Properties
     props.put("metadata.broker.list", brokerList)
     props.put("serializer.class", "kafka.serializer.StringEncoder")

     new ProducerConfig(props)
   }

   def consumerConfig = {
     val props = new Properties()
     props.put("zookeeper.connect", zkConnect)
     props.put("group.id", groupId)
     props.put("zookeeper.session.timeout.ms", "400")
     props.put("zookeeper.sync.time.ms", "200")
     props.put("auto.commit.interval.ms", "1000")

     props.put("auto.offset.reset", "smallest")

     new ConsumerConfig(props)
   }
 }
