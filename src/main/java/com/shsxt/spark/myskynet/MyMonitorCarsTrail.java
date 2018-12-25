package com.shsxt.spark.myskynet;

import com.shsxt.spark.util.DateUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Iskandar on 2018/12/25.
 *
 */
public class MyMonitorCarsTrail {
    /**
     * 0001卡口下的行车轨迹
     */
    public static void main(String[] agrs) {

        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local").setAppName("MonitorCarsTrail");
        JavaSparkContext context = new JavaSparkContext(sparkConf);
        JavaRDD<String> rdd1 = context.textFile("data/monitor_flow_action");

        rdd1 = rdd1.cache();
        JavaPairRDD<String, String> rdd2 = rdd1.mapToPair(new PairFunction<String, String, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String car = s.split("\t")[3];
                //System.out.println(car);
                return new Tuple2<>(car, s);
            }
        });

        JavaPairRDD<String, Iterable<String>> rdd3 = rdd2.groupByKey();
        rdd3.cache();

        JavaRDD<String> rddB = rdd1.filter(new Function<String, Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Boolean call(String v1) throws Exception {
                String sss = v1.split("\t")[1];
                //System.out.println(sss);
                Boolean b = sss.contains("0001");
                //System.out.println(b);
                return  b;
            }
        });

        JavaPairRDD<String, String> rddC = rddB.mapToPair(new PairFunction<String, String, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String car = s.split("\t")[3];
                return new Tuple2<>(car, car);
            }
        }).distinct();

        JavaPairRDD rddEnd = rddC.join(rdd3);

        rddEnd.foreach(new VoidFunction<Tuple2<String, Tuple2<String, Iterable<String>>>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void call(Tuple2<String, Tuple2<String, Iterable<String>>> t) throws Exception {
                String car = t._1();
                Iterable<String> s = t._2()._2();
                Iterator<String> iterator = s.iterator();
                List<String> list = IteratorUtils.toList(iterator);
                Collections.sort(list, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        String date1 = o1.split("\t")[4];
                        String date2 = o2.split("\t")[4];
                        boolean after = DateUtils.after(date1, date2);
                        if (after) {
                            return 1;

                        } else {
                            return -1;
                        }
                    }
                });

                String monitors = "";
                for (String s1 : list){
                    String[] strings = s1.split("\t");
                    monitors = monitors.concat(strings[1] ).concat(strings[2] ).concat(strings[4]) ;
                }

                System.out.println(car + "-" + monitors);
            }
        });


    }
}
