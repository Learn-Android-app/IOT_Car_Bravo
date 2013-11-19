package iot.mike.iotcarbravo.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Result_GPS {
	private String result;
	private Param param;
	
	
	private Result_GPS(){
		setResult("gps");
		param = new Param();
	}
	
	public static Result_GPS getInstance(){
		return Result_GPSHolder.result_GPS;
	}
	
	private static class Result_GPSHolder{
		public static Result_GPS result_GPS = new Result_GPS();
	}
	
	private static class Param{
		private double longtitude = 0;
		private double latitude = 0;
		private double direction = 0;
		private double speed = 0;
		private double height = 0;
		
		public void setHeight(double height){
			this.height = height;
		}
		
		public double getHeight(){
			return this.height;
		}
		
		public void setLongtitude (double longtitude){
			this.longtitude = longtitude;
		}
		
		public void setLatitude (double latitude){
			this.latitude = latitude;
		}
		
		public void setDirection(double direction){
			this.direction = direction;
		}
		
		public void setSpeed(double speed){
			this.speed = speed;
		}
		
		public double getLongtitude(){
			return longtitude;
		}
		
		public double getLatitude(){
			return latitude;
		}
		
		public double getDirection(){
			return direction;
		}
		
		public double getSpeed(){
			return speed;
		}
	}
	
	public double getHeight(){
		return param.getHeight();
	}
	
	public void setHeight(double height){
		param.setHeight(height);
	}
	
	public double getSpeed(){
		return param.getSpeed();
	}
	
	public double getDirection(){
		return param.getDirection();
	}
	
	public double getLongtitude(){
		return param.getLongtitude();
	}
	
	public double getLatitude(){
		return param.getLatitude();
	}
	
	public void setLongtitude (double longtitude){
		param.setLongtitude(longtitude);
	}
	
	public void setLatitude (double latitude){
		param.setLatitude(latitude);
	}
	
	public void setDirection(double direction){
		param.setDirection(direction);
	}
	
	public void setSpeed(double speed){
		param.setSpeed(speed);
	}
	
	public Result_GPS getResult_GPS(String jsonresult) {
		Result_GPS result_GPS = Result_GPS.getInstance();
		try {
			JSONObject param = new JSONObject(jsonresult).getJSONObject("param");			
			result_GPS.setLongtitude(param.getDouble("longtitude"));
			result_GPS.setSpeed(param.getDouble("speed"));
			result_GPS.setLatitude(param.getDouble("latitude"));
			result_GPS.setHeight(param.getDouble("height"));
			result_GPS.setDirection(param.getDouble("direction"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return result_GPS;
	}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
