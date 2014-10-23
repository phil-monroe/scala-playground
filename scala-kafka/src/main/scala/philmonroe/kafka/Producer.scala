package philmonroe.kafka

import kafka.producer.KeyedMessage

class Producer() {
   val producer = new kafka.producer.Producer[String, String](KafkaProperties.producerConfig)

   var msgCount = 0

   def send(topic: String, msg: String) = {
     producer.send(new KeyedMessage[String, String](topic, msgCount.toString, msg))
     msgCount += 1
   }

   def stop() = producer.close()
 }
