package iot.mike.iotcarbravo.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.xml;


public class Action_Emotor {
	private static class Action_EmotorHolder{
		public static Action_Emotor action_Emotor = new Action_Emotor();
	}
	
	public static Action_Emotor getInstance(){
		return Action_EmotorHolder.action_Emotor;
	}
	
	private static class Param{
		private int X = 0;
		private int Y = 0;
		
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
		
		public void reset(){
			this.setX(0);
			this.setY(0);
		}
		
		public synchronized void addSpeed(){
			Y = Y + 5;
			if (Y > 100) {
				Y = 100;
			}
		}
		
		public synchronized void reduceSpeed(){
			Y = Y - 5;
			if (Y < -100) {
				Y = -100;
			}
		}
		
		public synchronized void reduceTurn(){
			X = X - 5;
			if (X < -100) {
				X = -100;
			}
		}
		
		public synchronized void addTurn(){
			X = X + 5;
			if (X > 100) {
				X = 100;
			}
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
	
	public void reset(){
		param.reset();
	}
	
	public void addSpeed(){
		param.addSpeed();
	}
	
	public void addTurn(){
		param.addTurn();
	}
	
	public synchronized void setY(int y) {
		param.setY(y);
	}
	
	public synchronized void reduceSpeed(){
		param.reduceSpeed();
	}
	
	public synchronized void reduceTurn(){
		param.reduceTurn();
	}
	
	public int getX(){
		return param.getX();
	}
	
	public int getY(){
		return param.getY();
	}
	
}