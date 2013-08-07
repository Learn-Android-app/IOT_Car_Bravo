package iot.mike.activities;

import h264.com.VView;
import iot.mike.activities.R.id;
import iot.mike.data.Result_List;
import iot.mike.data.Result_USBCamera;
import iot.mike.mapview.OfflineMapView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;

public class TestActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		String string = "{\"result\":\"list\",\"param\":[\"close\",\"list\",\"okcamera\"]}";
		Result_List result_List = Result_List.getInstance();
		result_List = result_List.getResult_List(string);
		for (String test : result_List.getParams()) {
			Log.e(":" + test, ":" + test);
		}
		
		String string2 = "{\"result\":\"usbcamera\",\"param\":{\"frame\":\"BASE64编码的数据\"}}";
		Result_USBCamera result_USBCamera = Result_USBCamera.getInstance();
		result_USBCamera.getResult_USBCamera(string2);
		Log.e(string2, result_USBCamera.getFrame());
		
		
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
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(keyCode + ":", event.toString());
		TextView textView = (TextView)findViewById(R.id.textView1);
		textView.setText(event.toString());
		return super.onKeyDown(keyCode, event);
	}
	
}
