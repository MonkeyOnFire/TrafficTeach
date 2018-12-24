package com.shsxt.spark.rtmroad;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("partioner");

        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaPairRDD<String, String> javaPairRDD = sc.parallelizePairs(Arrays.asList(
                new Tuple2<>("1", "张三"),
                new Tuple2<>("2", "李四"),
                new Tuple2<>("3", "王五"),
                new Tuple2<>("4", "小明"),
                new Tuple2<>("5", "小红")
        ));

        javaPairRDD.mapValues(new Function<String, Integer>() {
            @Override
            public Integer call(String v1) throws Exception {
                return 1;
            }
        }).foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> tuple2) throws Exception {
                System.out.println(tuple2);
            }
        });

    }
}
