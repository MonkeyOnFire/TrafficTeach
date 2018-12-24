package com.shsxt.spark.dao.factory;

import com.shsxt.spark.dao.IAreaDao;
import com.shsxt.spark.dao.ICarTrackDAO;
import com.shsxt.spark.dao.IMonitorDAO;
import com.shsxt.spark.dao.IRandomExtractDAO;
import com.shsxt.spark.dao.ITaskDAO;
import com.shsxt.spark.dao.IWithTheCarDAO;
import com.shsxt.spark.dao.impl.AreaDaoImpl;
import com.shsxt.spark.dao.impl.CarTrackDAOImpl;
import com.shsxt.spark.dao.impl.MonitorDAOImpl;
import com.shsxt.spark.dao.impl.RandomExtractDAOImpl;
import com.shsxt.spark.dao.impl.TaskDAOImpl;
import com.shsxt.spark.dao.impl.WithTheCarDAOImpl;

/**
 * DAO工厂类
 * @author root
 *
 */
public class DAOFactory {
	
	
	public static ITaskDAO getTaskDAO(){
		return new TaskDAOImpl();
	}
	
	public static IMonitorDAO getMonitorDAO(){
		return new MonitorDAOImpl();
	}
	
	public static IRandomExtractDAO getRandomExtractDAO(){
		return new RandomExtractDAOImpl();
	}
	
	public static ICarTrackDAO getCarTrackDAO(){
		return new CarTrackDAOImpl();
	}
	
	public static IWithTheCarDAO getWithTheCarDAO(){
		return new WithTheCarDAOImpl();
	}

	public static IAreaDao getAreaDao() {
		return  new AreaDaoImpl();
		
	}
}
