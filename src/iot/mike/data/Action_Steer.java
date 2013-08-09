package iot.mike.data;

import org.json.JSONException;
import org.json.JSONObject;


public class Action_Steer {
	private static class Action_EmotorHolder{
		public static Action_Steer action_Steer = new Action_Steer();
	}
	
	public static Action_Steer getInstance(){
		return Action_EmotorHolder.action_Steer;
	}
	
	private static class Param{
		private int A;
		private int B;
		
		public synchronized void setA(int a){
			this.A = a;
		}
		
		public synchronized void setB(int b) {
			this.B = b;
		}
		
		public int getB(){
			return this.B;
		}
		
		public int getA(){
			return this.A;
		}
		
		public Param(){
			A = 0; B = 0;
		}
	}
	
	private String action;
	private Param param;
	
	private Action_Steer(){
		action = "steer";
		param = new Param();
	}
	
	public String getOrder() throws JSONException{
		JSONObject action = new JSONObject();
		JSONObject param = new JSONObject();
		
		param.put("A", this.getA());
		param.put("B", this.getB());
		
		action.put("action", this.action);
		action.put("param", param);
		
		return action.toString();
	}
	
	public void reset(){
		param.setA(0);
		param.setB(0);
	}
	
	public synchronized void setA(int a){
		param.setA(a);;
	}
	
	public synchronized void setB(int b) {
		param.setB(b);
	}
	
	public int getA(){
		return param.getA();
	}
	
	public int getB(){
		return param.getB();
	}
	
}