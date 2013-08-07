package iot.mike.net;

import iot.mike.data.Action_List;
import iot.mike.data.ResultType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;

public class NetUtil {
	private NetUtil(){}
	
	private static final String GPS_SETTING = "{\"action\":\"gps\"}";
	
	/**
	 * 用来检测返回值的类型
	 * @param jsonorder
	 * @return
	 * @throws JSONException
	 */
	public static int confirmType(String jsonorder) throws JSONException{
		String result_type = new JSONObject(jsonorder).getString("result");
		if (result_type.equals("okcamera")) {
			return ResultType.Result_OKCamera;
		}
		if (result_type.equals("usbcamera")) {
			return ResultType.Result_USBCamera;
		}
		if (result_type.equals("list")) {
			return ResultType.Result_List;
		}
		if (result_type.equals("gps")) {
			return ResultType.Result_GPS;
		}
		return ResultType.Result_NONE;
	}
	
	/**
	 * 将list的信息发出去
	 * @param writer socket写出流
	 * @return Message
	 */
	public static Message sendList(BufferedWriter writer){
		Message message = new Message();
		message.what = ResultType.Result_List;
		Action_List action_List = Action_List.getInstance();
		try {
			String list = action_List.getOrder();
			message.obj = list;
			writer.write(list + "\n"); 
			writer.flush();
		} catch (JSONException e) {
			e.printStackTrace();
			message.obj = null;
		} catch (IOException e) {
			e.printStackTrace();
			message.obj = null;
		}
		return message;
	}
	
	/**
	 * 将GPS设置发粗去
	 * @param writer Socket写出流
	 * @throws IOException
	 */
	public static void sendGPSSetting(OutputStreamWriter writer) 
			throws IOException{
		synchronized (writer) {
			writer.write(GPS_SETTING + "\n");
			writer.flush();
		}
	}
}
