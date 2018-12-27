package com.shsxt.spark.myskynet;

import com.google.common.base.Optional;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.*;

/**
 * Created by Iskandar on 2018/12/25.
 *
 */
public class MyMonitorFlowAnalyze {
    /**
     * 卡口状态监控：异常卡口数、正常卡口数
     * 摄像头状态：异常、正常
     * 卡口流量topN
     *
     * @param args
     */
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("MyMonitorFlowAnalyze");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<String> rddA1 = sparkContext.textFile("data/monitor_flow_action");
        JavaPairRDD<String, String> rddA2 = rddA1.mapToPair(new PairFunction<String, String, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String[] ss = s.split("\t");
                return new Tuple2<>(ss[1], s);
            }
        });
        JavaPairRDD<String, Iterable<String>> rddA3 = rddA2.groupByKey();
        JavaPairRDD<String, String> rddA4 = rddA3.mapToPair(new PairFunction<Tuple2<String, Iterable<String>>, String, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Iterable<String>> t) throws Exception {
                Iterator<String> iterator = t._2().iterator();
                String cameraInfo = "";
                int cameraCount = 0;
                int carCount = 0;
                Set set = new HashSet<String>();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    String[] ss = s.split("\t");
                    if (set.contains(ss[2])) {
                        cameraCount++;
                        cameraInfo = cameraInfo + ss[2];
                        set.add(ss[2]);
                    }
                    carCount++;
                }
                String result = cameraInfo + "|" + cameraCount + "|" + carCount;
                return new Tuple2<>(t._1(), result);
            }
        });

        rddA4 = rddA4.cache();

        JavaPairRDD<Integer,String> rddA5 =  rddA4.mapToPair(new PairFunction<Tuple2<String, String>, Integer, String>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<Integer, String> call(Tuple2<String, String> t) throws Exception {
                Integer carNum = Integer.parseInt(t._2().split("\\|")[2]);
                return new Tuple2<>(carNum,t._1());
            }
        }).sortByKey(false);

        List<Tuple2<Integer,String>> list = rddA5.take(5);
        int i = 1;
        for (Tuple2<Integer,String> t : list){
            System.out.println("流量第"+i+"的卡口为"+ t._2() +"，车流量为："+ t._1());
            i++;
        }

        JavaRDD<String> rddB1 = sparkContext.textFile("data/monitor_camera_info");
        JavaPairRDD<String,String> rddB2 = rddB1.mapToPair(new PairFunction<String, String, String>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String[] ss = s.split("\t");
                return new Tuple2<>(ss[0],ss[1]);
            }
        });
        JavaPairRDD<String,Iterable<String>> rddB3 = rddB2.groupByKey();

        JavaPairRDD<String,String> rddB4 = rddB3.mapToPair(new PairFunction<Tuple2<String, Iterable<String>>, String, String>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<String, String> call(Tuple2<String, Iterable<String>> t) throws Exception {
                Iterator<String> iterator = t._2.iterator();
                int cameraCount = 0;
                String cameraInfo = "";
                while (iterator.hasNext()){
                    String s = iterator.next();
                    cameraInfo = cameraInfo + "摄像头：" + s + "，";
                    cameraCount++;
                }
                cameraInfo = cameraInfo.substring(0,cameraInfo.length()-1);
                return new Tuple2<>(t._1,cameraInfo+"|"+cameraCount);
            }
        });

        Accumulator<Integer> errMonitor = sparkContext.accumulator(0);
        Accumulator<Integer> nomalMonitor = sparkContext.accumulator(0);
        Accumulator<Integer> errCamera = sparkContext.accumulator(0);
        Accumulator<Integer> nomalCamera = sparkContext.accumulator(0);
        JavaPairRDD<String,Tuple2<String,Optional<String>>> joinRdd = rddB4.leftOuterJoin(rddA4);
        joinRdd.foreach(new VoidFunction<Tuple2<String, Tuple2<String, Optional<String>>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<String, Optional<String>>> t) throws Exception {
                Optional o = t._2._2;
                
            }
        });







    }
}
