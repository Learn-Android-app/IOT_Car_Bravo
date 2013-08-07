package iot.mike.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Result_USBCamera {
	private String action;
	private Param param;
	
	private static class Param{
		private String frame;
		
		public String getFrame(){
			return this.frame;
		}
		
		public void setFrame(String frame){
			this.frame = frame;
		}
	}
	
	private Result_USBCamera(){
		this.action = "usbcamera";
		this.param = new Param();
	}
	
	private static class Result_USBCameraHolder{
		public static Result_USBCamera result_USBCamera = new Result_USBCamera();
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

