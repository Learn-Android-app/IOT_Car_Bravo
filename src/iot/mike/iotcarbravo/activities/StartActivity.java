package iot.mike.iotcarbravo.activities;

import iot.mike.iotcarbravo.setting.SettingData;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {
	private Button 			KeyKoardAcitvityButton;
	private Button 			NoKeyBoardActivityButton;
	private	boolean 		isOK							= false;
	private ProgressDialog	progressDialog					= null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		Message message = new Message();
		message.what = 0;
		startHandler.sendMessage(message);
		//-------------------转圈
		ReadSetting();
		loadThread.start();
		KeyKoardAcitvityButton = (Button)findViewById(R.id.keyboard_BTN);
		KeyKoardAcitvityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), KeyBoradActivity.class);
				if (!isOK) {
					System.exit(2);
				}
				SettingData.CtrlMode = SettingData.KeyBoard;
				startActivity(intent);
			}
		});
		
		NoKeyBoardActivityButton = (Button)findViewById(R.id.nokeyboard_BTN);
		NoKeyBoardActivityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), KeyBoradActivity.class);
				SettingData.CtrlMode = SettingData.NoKeyBoard;
				if (!isOK) {
					System.exit(2);
				}
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem actionItem = menu.add(0, Menu.FIRST, 0, "设置");
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return (    super.onCreateOptionsMenu(menu));
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {        
            case Menu.FIRST:{
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        }
        return true;
	}
	
	private void ReadSetting(){
		@SuppressWarnings("deprecation")
        SharedPreferences spc = 
				getSharedPreferences("iot.mike.iotcarbravo.activities_preferences",
						MODE_WORLD_WRITEABLE);
		SettingData.CarIP = spc.getString("CarIP", "192.168.0.232");
		SettingData.CarMainPort = Integer.valueOf(spc.getString("CarMainPort", "7890"));
		
    	Log.e("读取的数据：", "IP:" + SettingData.CarIP +"PORT:" + SettingData.CarMainPort);
	}
	
	private Thread loadThread = new Thread(new Runnable() {
		@Override
		public void run() {
			String fileDir = Environment.getExternalStorageDirectory()
					+ File.separator + "IOT.MIKE.CAR" 
					+ File.separator + "GPS.S";
			File GPS_S = new File(fileDir);
			System.out.println(GPS_S.exists());
			if (!GPS_S.exists()) {
				Message message = new Message();
				message.what = 2;
				startHandler.sendMessage(message);
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			isOK = true;
			Message message = new Message();
			message.what = 1;
			startHandler.sendMessage(message);
		}
	});

	
	private Handler startHandler = new Handler(){
		@Override
		public void handleMessage(Message message) {
			if (message.what == 2) {
				Dialog dialog = onCreateDialog(2);
				dialog.show();
			}else if (message.what == 1) {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}else if (message.what == 0){
				if (progressDialog == null) {
					progressDialog= (ProgressDialog)onCreateDialog(0);
					progressDialog.show();
				}
			}
		}
	};
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
	        case 2:{
	        	Dialog dialog = new AlertDialog.Builder(StartActivity.this)
	        			.setTitle("警告！！！")
	        			.setMessage("未能找到配置文件！")
	        			.setNegativeButton("退出程序", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								System.exit(0);
							}
						})
						.create();
	        	dialog.setCanceledOnTouchOutside(false);
	        	return dialog;
	        }
	        
            default: {         //有标题栏的进度对话框
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("正在初始化.....");
                dialog.setMessage("Please wait while loading maps and setting...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                return dialog;
            }
        }
    }
}