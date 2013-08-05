package iot.mike.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Action_USBCamera {
	private Action_USBCamera(){}
	
	private static class Action_USBCameraHolder{
		public static Action_USBCamera action_OKCamera = new Action_USBCamera();
	}
	
	public static Action_USBCamera getInstance(){
		return Action_USBCameraHolder.action_OKCamera;
	}
	
	private static class Param{
		private CameraMode mode = CameraMode.on;
		
		public synchronized void setMode(CameraMode mode){
			this.mode = mode;
		}
		
		public synchronized CameraMode getMode(){
			return this.mode;
		}
	}

	public void setMode(CameraMode mode){
		param.setMode(mode);
	}
	
	public CameraMode getMode(){
		return param.getMode();
	}
	
	public String getOrder() throws JSONException{
		JSONObject okCamera = new JSONObject();
		JSONObject param = new JSONObject();

		param.put("mode", getMode());
		okCamera.put("action", action);
		okCamera.put("param", param);
		
		return okCamera.toString();
	}
	
	private String action = "usbcamera";
	private Param param = new Param();
}	
