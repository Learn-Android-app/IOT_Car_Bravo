package iot.mike.iotcarbravo.mapview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Canvas;
import android.graphics.Color;

public class GPSTest {
	private static final File GPS_SOURCE = new File("GPS.S");
	private static HashMap<GPSLocation, GPSLocation> gpsHashMap = new HashMap<GPSLocation, GPSLocation>();
	private static LinkedList<Double> list = new LinkedList<Double>();
	
	private static HashMap<GPSData, LinkedList<GPSLocation>> gpsMetadata = new HashMap<GPSData, LinkedList<GPSLocation>>();
	
	private static LinkedList<GPSData> datas = new LinkedList<GPSData>();
	
	private static double maxLatitudeD = 0;
	private static double minLatitudeD = 0;
	private static double maxLongtitudeD = 0;
	private static double minLongtitudeD = 0;
	
	private static LinkedList<Double> longtitudes = new LinkedList<Double>();
	
	private static int DLatitude = 0;
	private static int DLongtitude = 0;
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = 
				new BufferedReader(new FileReader(GPS_SOURCE));
		long time = System.currentTimeMillis();
		String data = null;
		GPSLocation[] gps = new GPSLocation[2];
		
		while ((data = reader.readLine()) != null) {
			gps = getGPS(data);
			//gpsHashMap.put(gps[0], gps[1]);
		}
		System.out.println("耗时(s):" + (System.currentTimeMillis() - time)/1000.0 + "秒");
		System.out.println("漂移库大小:" + gpsHashMap.size());
		System.out.println("搜素测试开始:");
		
		System.out.println(datas.size());
		
		time = System.currentTimeMillis();
		Object[] d = longtitudes.toArray();	
		Arrays.sort(d);
		
		for (Object object : d) {
			System.out.println(object);
		}
		
		
		GPSLocation search = new GPSLocation();
		search.setLatitude(31.3306);
		search.setLongtitude(120.6349);
		
		System.out.println("查找结果:" + gpsHashMap.containsKey(search));
		System.out.println("转换结果:" + gpsHashMap.get(search));
		System.out.println("搜索耗时:" + (System.currentTimeMillis() - time)/1000.0);
		reader.close();
		
	}
	
	private static GPSLocation[] getGPS(String data) {
		Pattern pattern = Pattern.compile("(.*)( )(.*)(:)(.*) (.*)");
		double preLatitude, preLongtitude, newLatitude, newLongtitude;
		Matcher matcher = pattern.matcher(data);
		GPSLocation preGPS = new GPSLocation();
		GPSLocation newGPS = new GPSLocation();
		if (matcher.find()) {
			preLongtitude = Double.valueOf(matcher.group(1));
			preLatitude = Double.valueOf(matcher.group(3));
			newLongtitude= Double.valueOf(matcher.group(5));
			newLatitude = Double.valueOf(matcher.group(6));
			
			preGPS.setLatitude(preLatitude);
			preGPS.setLongtitude(preLongtitude);
			newGPS.setLatitude(newLatitude);
			newGPS.setLongtitude(newLongtitude);
			
			DLatitude = (int)((preLatitude - newLatitude) * 10000);
			DLongtitude = (int)((preLongtitude - newLongtitude) * 10000);
			
			GPSData data2 = new GPSData();
			data2.setyD(DLatitude);
			data2.setxD(((int)(preLatitude * 10000) - 310000)/5);
			
			System.out.println(data2.getxD() + ":" + data2.getyD());
			
			datas.add(data2);
			
			/**if (DLatitude == 21
					&& DLongtitude == -41) {
				//System.out.println(preLatitude + ":" + preLongtitude);
				if (preLatitude == 31.2423) {
					longtitudes.add(preLongtitude);
				}
			}
			**/
			
			/**
			if (maxLatitudeD == 0 && minLatitudeD == 0) {
				maxLatitudeD = DLatitude;
				minLatitudeD = DLatitude;
			}
			
			if (maxLongtitudeD == 0 && minLongtitudeD == 0) {
				maxLongtitudeD = DLongtitude;
				minLongtitudeD = DLongtitude;
			}
			
			if (DLatitude > maxLatitudeD) {
				maxLatitudeD = DLatitude;
			}else if (DLatitude < minLatitudeD) {
				minLatitudeD = DLatitude;
			}
			
			if (DLongtitude > maxLongtitudeD) {
				maxLongtitudeD = DLongtitude;
			}else if (DLongtitude < minLongtitudeD) {
				minLongtitudeD = DLongtitude;
			}
			**/
		}
		return new GPSLocation[]{preGPS, newGPS};
	}
}
