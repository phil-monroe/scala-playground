package philmonroe.spark

import org.apache.spark.{SparkContext, SparkConf}

object SparkTest extends App {
  val conf = new SparkConf()
  conf.setMaster("yarn-client")
  conf.setAppName("Phil Test")

  val sc = new SparkContext(conf)
}
