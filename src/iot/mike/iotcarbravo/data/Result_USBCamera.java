package iot.mike.iotcarbravo.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public class Result_USBCamera {
	private String action;
	private Param param;
	
	private static class Param{
		private String frame;
		private byte[] frameData;
		
		public byte[] getFrameData(){
			return frameData;
		}
		
		public String getFrame(){
			return this.frame;
		}
		
		public void setFrame(String frame){
			this.frame = frame;
			if (this.frame != null) {
				frameData = Base64.decode(this.frame, 0);
			}
		}
	}
	
	private Result_USBCamera(){
		this.action = "usbcamera";
		this.param = new Param();
	}
	
	private static class Result_USBCameraHolder{
		public static Result_USBCamera result_USBCamera = 
				new Result_USBCamera();
	}
	
	public static Result_USBCamera getInstance(){
		return Result_USBCameraHolder.result_USBCamera;
	}
	
	public String getFrame(){
		return param.getFrame();
	}
	
	public void setFrame(String frame){
		param.setFrame(frame);
	}
	
	public byte[] getFrameData(){
		return param.getFrameData();
	}
	
	public Result_USBCamera getResult_USBCamera(String jsonorder){
		Result_USBCamera result_USBCamera = Result_USBCamera.getInstance();
		
		try {
			JSONObject paramJsonObject = 
					new JSONObject(jsonorder).getJSONObject("param");
			result_USBCamera.setFrame(paramJsonObject.getString("frame"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result_USBCamera;
	}
}

