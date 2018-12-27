package com.shsxt.spark.myskynet;

import com.shsxt.spark.conf.ConfigurationManager;
import com.shsxt.spark.constant.Constants;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 实时监控道路拥堵情况
 */
public class MyRoadRealTimeAnalyze {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("AdClickRealTimeStatSpark").setMaster("local[2]");

        JavaStreamingContext context = new JavaStreamingContext(conf,Durations.seconds(5));
        context.checkpoint("/checkpoint");
        Map<String, String> kafkaParams = new HashMap<>();
        String brokers = ConfigurationManager.getProperty((Constants.KAFKA_METADATA_BROKER_LIST));
        kafkaParams.put("metadata.broker.list",brokers);

        //构建topic set
        String kafkaTopics = ConfigurationManager.getProperty(Constants.KAFKA_TOPICS);



    }
}
