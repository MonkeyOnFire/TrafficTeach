package com.spark.spark.test;

import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ParTionerTest {

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




        javaPairRDD.mapPartitionsWithIndex(new Function2<Integer, Iterator<Tuple2<String, String>>, Iterator<String>>() {

            @Override
            public Iterator<String> call(Integer integer, Iterator<Tuple2<String, String>> tuple2Iterator) throws Exception {
                List<String> list = new ArrayList<>();
                while(tuple2Iterator.hasNext()){
                    System.out.println("index: " + integer + " value" + tuple2Iterator.next());
                }
                return list.iterator();
            }
        },true).collect();



        JavaPairRDD<String, String> partitionByPairRDD = javaPairRDD.partitionBy(new Partitioner() {
            /**
             * 定义分区规则
             * @param key
             * @return
             */
            @Override
            public int getPartition(Object key) {

                Integer value = Integer.parseInt((String)key);

                return value % 3;
            }

            /**
             * 定义分成几个区
             *
             * @return
             */
            @Override
            public int numPartitions() {
                return 3;
            }
        });

        partitionByPairRDD.mapPartitionsWithIndex(new Function2<Integer, Iterator<Tuple2<String, String>>, Iterator<String>>() {

            @Override
            public Iterator<String> call(Integer integer, Iterator<Tuple2<String, String>> tuple2Iterator) throws Exception {
                List<String> list = new ArrayList<>();
                while(tuple2Iterator.hasNext()){
                    System.out.println("index: " + integer + " value" + tuple2Iterator.next());
                }
                return list.iterator();
            }
        },true).collect();


    }
}
