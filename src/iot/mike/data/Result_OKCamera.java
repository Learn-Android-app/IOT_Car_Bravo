package iot.mike.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Result_OKCamera {
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

