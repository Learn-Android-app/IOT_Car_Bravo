package iot.mike.iotcarbravo.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.util.Log;

public class Result_List {
	private String result;
	private String[] param;
	
	private Result_List(){
		result = "list";
	}
	
	private static class Result_ListHolder{
		public static Result_List result_List = new Result_List();
	}
	
	public static Result_List getInstance(){
		return Result_ListHolder.result_List;
	}
	
	
	public String getResult(){
		return this.result;
	}
	
	public String[] getParams(){
		return this.param;
	}
	
	private void setParam(String[] param){
		this.param = param;
	}
	
	/**
	 * List返回结果
	 * @param jsonorder
	 * @return
	 */
	public Result_List getResult_List(String jsonorder){
		Log.v("List_Result", jsonorder);
		Result_List result_List = Result_List.getInstance();
		try {
			JSONArray params = new JSONObject(jsonorder).
					getJSONArray("param");
			String[] paramStrings = new String[params.length()];
			for (int i = 0; i < params.length(); i++) {
				Log.d("Test" + i, params.getString(i));
				paramStrings[i] = params.getString(i);
			}
			result_List.setParam(paramStrings);
		} catch (JSONException e) {
			e.printStackTrace();
			result_List.setParam(null);
		}
		return result_List;
	}
}
