package iot.mike.iotcarbravo.mapview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPSTranUTIL {
	private static HashMap<GPSLocation, GPSLocation> gpsHashMap = new HashMap<GPSLocation, GPSLocation>();
	
	public static void createHashMap(File gpsfile) throws IOException {
		BufferedReader reader = 
				new BufferedReader(new FileReader(gpsfile));
		long time = System.currentTimeMillis();
		String data = null;
		GPSLocation[] gps = new GPSLocation[2];
		
		while ((data = reader.readLine()) != null) {
			gps = getGPS(data);
			gpsHashMap.put(gps[0], gps[1]);
		}
		System.out.println("耗时(s):" + (System.currentTimeMillis() - time)/1000.0 + "秒");
		System.out.println("漂移库大小:" + gpsHashMap.size());
		
		System.out.println("搜素测试开始:");
		time = System.currentTimeMillis();
		
		GPSLocation search = new GPSLocation();
		search.setLatitude(31.3114);
		search.setLongtitude(120.6349);
		
		System.out.println("查找结果:" + gpsHashMap.containsKey(search));
		System.out.println("转换结果:" + gpsHashMap.get(search));
		System.out.println("搜索耗时:" + (System.currentTimeMillis() - time)/1000.0);
		reader.close();
	}
	
	public static GPSLocation[] getGPS(String data) {
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
		}
		return new GPSLocation[]{preGPS, newGPS};
	}
}
