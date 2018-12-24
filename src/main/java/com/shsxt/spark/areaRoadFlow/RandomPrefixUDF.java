package com.shsxt.spark.areaRoadFlow;

import java.util.Random;

import org.apache.spark.sql.api.java.UDF2;

public class RandomPrefixUDF implements UDF2<String, Integer, String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    //val = 松江:20      ranNum=10
    //5_松江:20  6_松江:20
	@Override
	public String call(String val, Integer ranNum) throws Exception {
		Random random = new Random();
		int prefix = random.nextInt(ranNum);
		return prefix+"_"+val;
	}

}
