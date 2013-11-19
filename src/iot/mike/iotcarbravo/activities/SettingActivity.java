package iot.mike.iotcarbravo.activities;

import iot.mike.iotcarbravo.setting.SettingData;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

@SuppressWarnings("deprecation")
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
			SharedPreferences spc = 
					getSharedPreferences("iot.mike.iotcarbravo.activities_preferences",
							MODE_WORLD_WRITEABLE);
			SettingData.CarIP = spc.getString("CarIP", "");
			SettingData.CarMainPort = Integer.valueOf(spc.getString("CarMainPort", "7890"));
			Log.v("设置界面读取的数据是:", SettingData.CarIP + ":" + SettingData.CarMainPort);
		}
}
