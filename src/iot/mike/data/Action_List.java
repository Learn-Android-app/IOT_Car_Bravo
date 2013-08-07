package iot.mike.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Action_List {
	private String action = "list";

	private Action_List(){}
	
	private static class Action_ListHolder{
		public static Action_List action_List = new Action_List();
	}
	
	public static Action_List getInstance() {
		return Action_ListHolder.action_List;
	}
	
	public String getOrder() throws JSONException{
		JSONObject list = new JSONObject();
		list.put("action", action);
		return list.toString();
	}
}
