package philmonroe.kafka

import kafka.admin.AdminUtils
import kafka.utils.ZKStringSerializer
import org.I0Itec.zkclient.ZkClient

import scala.collection.mutable

object KafkaUtils {
  val zk = new ZkClient(KafkaProperties.zkConnect)
  zk.setZkSerializer(ZKStringSerializer)

  val topicCache = mutable.Set.empty[String]

  def ensureTopicCreated(topic: String) {
    if (!topicCache.contains(topic))
      if (!AdminUtils.topicExists(zk, topic))
        AdminUtils.createTopic(zk, topic, 1, 1)
    topicCache += topic
  }
}
