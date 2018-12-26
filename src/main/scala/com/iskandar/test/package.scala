package com.iskandar

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 郭镇 on 2018/12/26.
  */
package object MymonitorCarsTrail {
  /**
    * 0001卡口下的行车轨迹
    */
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("lacal").setAppName("MonitorCarsTrail")
    val sc = new SparkContext(conf)
    val rdd1 = sc.textFile("data/MonitorCarsTrail")


  }
}
