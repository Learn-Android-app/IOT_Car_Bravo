package iot.mike.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import iot.mike.data.Action_Close;
import iot.mike.data.ResultType;
import iot.mike.data.Result_GPS;
import iot.mike.net.SocketManager;
import iot.mike.setting.SettingActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private SocketManager socketManager = SocketManager.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		socketManager.setHandler(mainHandler);
		
		Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
		//startActivity(intent);
		try {
			FileInputStream reader = new FileInputStream(
					new File(Environment.getExternalStorageDirectory().toString() 
							+ File.separator + "wubin64.base64"));
			FileOutputStream writer = new FileOutputStream(
					new File(Environment.getExternalStorageDirectory().toString() 
							+ File.separator + "wubin64.h264"));
			int size = 0;
			byte[] buffer = new byte[254800];
			while ((size = reader.read(buffer)) != -1) {
				String aString = new String(buffer, 0, size);
				byte[] dataout = Base64.decode(aString, 0);
				buffer = null;
				buffer = new byte[254800];
				writer.write(dataout);
				writer.flush();
			}
			writer.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private Handler mainHandler = new Handler(){
		@Override
		public void handleMessage(Message message){
			switch (message.what) {
				case ResultType.Result_GPS:{
					
					break;
				}
				
				case ResultType.Result_OKCamera:{
					
					break;
				}
				
				case ResultType.Result_List:{
					
					break;
				}
				
				case ResultType.Result_USBCamera:{
					
					break;
				}
				
				default:{
					
					break;
				}
			}
		}
	};
}
