package iot.mike.data;

import org.json.JSONException;
import org.json.JSONObject;


public class Action_Emotor {
	private static class Action_EmotorHolder{
		public static Action_Emotor action_Emotor = new Action_Emotor();
	}
	
	public static Action_Emotor getInstance(){
		return Action_EmotorHolder.action_Emotor;
	}
	
	private static class Param{
		private int X;
		private int Y;
		
		public synchronized void setX(int x){
			this.X = x;
		}
		
		public synchronized void setY(int y) {
			this.Y = y;
		}
		
		public int getX(){
			return this.X;
		}
		
		public int getY(){
			return this.Y;
		}
		
		public Param(){
			X = 0; Y = 0;
		}
	}
	
	private String action;
	private Param param;
	
	private Action_Emotor(){
		action = "emotor";
		param = new Param();
	}
	
	public String getOrder() throws JSONException{
		JSONObject action = new JSONObject();
		JSONObject param = new JSONObject();
		
		param.put("X", this.getX());
		param.put("Y", this.getY());
		
		action.put("action", this.action);
		action.put("param", param);
		
		return action.toString();
	}
	
	public synchronized void setX(int x){
		param.setX(x);
	}
	
	public synchronized void setY(int y) {
		param.setY(y);
	}
	
	public int getX(){
		return param.getX();
	}
	
	public int getY(){
		return param.getY();
	}
	
}