package iot.mike.iotcarbravo.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Action_OKCamera {
	private Action_OKCamera(){}
	
	private static class Action_OKCameraHolder{
		public static Action_OKCamera action_OKCamera = new Action_OKCamera();
	}
	
	public static Action_OKCamera getInstance(){
		return Action_OKCameraHolder.action_OKCamera;
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
	
	private String action = "okcamera";
	private Param param = new Param();
}	
