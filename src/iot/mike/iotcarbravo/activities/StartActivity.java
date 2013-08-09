package iot.mike.iotcarbravo.activities;

import iot.mike.iotcarbravo.setting.SettingData;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	private void ReadSetting(){
		SharedPreferences spc = 
				getSharedPreferences("iot.mike.activities_preferences",
						MODE_WORLD_WRITEABLE);
		SettingData.CarIP = spc.getString("CarIP", "192.168.135.116");
    	Log.e("读取的IP为:", "IP:" + SettingData.CarIP);
	}
}
