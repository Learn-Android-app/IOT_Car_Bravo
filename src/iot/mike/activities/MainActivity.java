package iot.mike.activities;

import h264.com.VView;
import iot.mike.data.ResultType;
import iot.mike.data.Result_List;
import iot.mike.data.Result_USBCamera;
import iot.mike.mapview.OfflineMapView;
import iot.mike.net.SocketManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;

/**
 * 使用键盘和头盔跟踪模块的活动
 * @author mikecoder
 * @date 2013-08-06
 */
public class MainActivity extends Activity {
	private SocketManager socketManager = SocketManager.getInstance();
	private OfflineMapView mapView;
	private VView videoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keyboard);
		initViews();
		
		socketManager.setKeyBoardActivityHandler(KeyBoardActivityHandler);
		socketManager.startLink();
		
		Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
		//startActivity(intent);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//键盘数据
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(keyCode + ":", event.toString());
		return false;
	}
	
	private Handler KeyBoardActivityHandler = new Handler(){
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
	
	private void initViews(){
		videoView = (VView)findViewById(R.id.video_VV);
		mapView = (OfflineMapView)findViewById(R.id.offlineMap_MAP);
		mapView.setLocation(120.64248919487, 31.30230587142129, 18, 0, 20, 20);
	}
}


