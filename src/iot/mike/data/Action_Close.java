package iot.mike.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Action_Close {
	private String action = "close";
	
	private Action_Close(){}
	
	private static class Action_ListHolder{
		public static Action_Close action_List = new Action_Close();
	}
	
	public static Action_Close getInstance() {
		return Action_ListHolder.action_List;
	}
	
	public String getOrder() throws JSONException{
		JSONObject list = new JSONObject();
		list.put("action", action);
		return list.toString();
	}
}
