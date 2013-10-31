package iot.mike.iotcarbravo.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public class Result_OKCamera {
	private String action;
	private Param param;
	
	private static class Param{
		private String frame;
		private byte[] frameData;
		public String getFrame(){
			return this.frame;
		}
		
		public void setFrame(String frame){
			this.frame = frame;
			if (this.frame != null) {
				frameData = Base64.decode(this.frame, 0);
			}
		}
		
		public byte[] getFrameData(){
			return frameData;
		}
	}
	
	private Result_OKCamera(){
		this.action = "okcamera";
		this.param = new Param();
	}
	
	private static class Result_OKCameraHolder{
		public static Result_OKCamera result_OKCamera = new Result_OKCamera();
	}
	
	public static Result_OKCamera getInstance(){
		return Result_OKCameraHolder.result_OKCamera;
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
	
	public Result_OKCamera getRsult_OKCamera(String jsonorder){
		Result_OKCamera result_OKCamera = Result_OKCamera.getInstance();
		try {
			JSONObject paramJsonObject = 
					new JSONObject(jsonorder).getJSONObject("param");
			result_OKCamera.setFrame(paramJsonObject.getString("frame"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result_OKCamera;
	}
}

