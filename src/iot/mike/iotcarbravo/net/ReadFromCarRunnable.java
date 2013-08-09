package iot.mike.iotcarbravo.net;

import iot.mike.iotcarbravo.data.ResultType;
import iot.mike.iotcarbravo.data.Result_GPS;
import iot.mike.iotcarbravo.data.Result_List;
import iot.mike.iotcarbravo.data.Result_OKCamera;
import iot.mike.iotcarbravo.data.Result_USBCamera;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReadFromCarRunnable implements Runnable {
	private BufferedReader reader;
	private Handler MainActivityHandler;
	private boolean isStart;
	
	
	
	public void end(){
		isStart = false;
	}
	
	public ReadFromCarRunnable(BufferedReader reader, 
			Handler keyboardactivityhandler){
		this.isStart = true;
		this.reader = reader;
		this.MainActivityHandler = keyboardactivityhandler;
	}
	
	@Override
	public void run() {
		try {
			String inputData = "";
			while (isStart) {
				inputData = reader.readLine();
				if (inputData.length() !=0) {
					try {
						int type = NetUtil.confirmType(inputData);
						//处理命令
						dealJsonOrder(inputData, type);
					} catch (JSONException e) {
						//解析出现问题
						e.printStackTrace();
						continue;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	/**
	 * 具体的处理数据
	 * @param jsonorder
	 * @param type
	 */
	private void dealJsonOrder(String jsonorder, int type){
		switch (type) {
			case ResultType.Result_GPS:{
				Result_GPS result_GPS = Result_GPS.getInstance();
				result_GPS = result_GPS.getResult_GPS(jsonorder);
				Message message = new Message();
				message.what = ResultType.Result_GPS;
				synchronized (MainActivityHandler) {
					MainActivityHandler.sendMessage(message);
				}
				break;
			}
			
			case ResultType.Result_OKCamera:{
				Result_OKCamera result_OKCamera = Result_OKCamera.getInstance();
				result_OKCamera = result_OKCamera.getRsult_OKCamera(jsonorder);
				Message message = new Message();
				message.what = ResultType.Result_OKCamera;
				synchronized (MainActivityHandler) {
					MainActivityHandler.sendMessage(message);
				}
				break;
			}
			
			case ResultType.Result_USBCamera:{
				Result_USBCamera result_USBCamera = Result_USBCamera.getInstance();
				result_USBCamera = result_USBCamera.getResult_USBCamera(jsonorder);
				Message message = new Message();
				message.what = ResultType.Result_USBCamera;
				synchronized (MainActivityHandler) {
					MainActivityHandler.sendMessage(message);
				}
				break;
			}
	
			case ResultType.Result_List:{
				Result_List result_List = Result_List.getInstance();
				result_List = result_List.getResult_List(jsonorder);
				Message message = new Message();
				message.what = ResultType.Result_List;
				synchronized (MainActivityHandler) {
					MainActivityHandler.sendMessage(message);
				}
				break;
			}
			
			default:{
				//Do Nothing
			}
		}
	}

}
