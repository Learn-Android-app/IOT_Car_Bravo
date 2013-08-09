package iot.mike.iotcarbravo.activities;

import iot.mike.iotcarbravo.setting.SettingData;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class SettingActivity extends PreferenceActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    	//requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    	//以上代码使用来全屏显示
	    	
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.setting_preference);
	  }
	  
	  @Override
	  public void onStop(){
		  updateData();
		  super.onStop();
	  }
	  
	  @Override
	  public void onDestroy(){
		  updateData();
		  super.onDestroy();
	  }
	
	  public void updateData(){
			Log.e(SettingData.CarIP, SettingData.CarIP);
			SharedPreferences spc = 
					getSharedPreferences("iot.mike.activities_preferences",
							MODE_WORLD_WRITEABLE);
			SettingData.CarIP = spc.getString("CarIP", "192.168.135.116");
			SettingData.CarMainPort = spc.getInt("CarMainPort", 7890);
	    	Log.e("读取的IP为:", "IP:" + SettingData.CarIP);
		}
}
