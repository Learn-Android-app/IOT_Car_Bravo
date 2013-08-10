package iot.mike.iotcarbravo.activities;

import iot.mike.iotcarbravo.setting.SettingData;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {
	private Button KeyKoardAcitvityButton;
	private Button NoKeyBoardActivityButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		ReadSetting();
		KeyKoardAcitvityButton = (Button)findViewById(R.id.keyboard_BTN);
		KeyKoardAcitvityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), KeyBoradActivity.class);
				SettingData.CtrlMode = SettingData.KeyBoard;
				startActivity(intent);
				finish();
			}
		});
		
		NoKeyBoardActivityButton = (Button)findViewById(R.id.nokeyboard_BTN);
		NoKeyBoardActivityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), KeyBoradActivity.class);
				SettingData.CtrlMode = SettingData.NoKeyBoard;
				startActivity(intent);
				finish();
			}
		});
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
		SettingData.CarMainPort = spc.getInt("CarMainPort", 7890);
		
    	Log.e("读取的IP为:", "IP:" + SettingData.CarIP);
	}
}
