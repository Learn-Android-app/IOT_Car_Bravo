package iot.mike.iotcarbravo.activities;

import h264.com.VView;
import iot.mike.iotcarbravo.data.Action_Emotor;
import iot.mike.iotcarbravo.data.Action_Steer;
import iot.mike.iotcarbravo.data.ResultType;
import iot.mike.iotcarbravo.data.Result_GPS;
import iot.mike.iotcarbravo.data.Result_List;
import iot.mike.iotcarbravo.data.Result_OKCamera;
import iot.mike.iotcarbravo.data.Result_USBCamera;
import iot.mike.iotcarbravo.mapview.OfflineMapView;
import iot.mike.iotcarbravo.net.SocketManager;
import iot.mike.iotcarbravo.setting.SettingData;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class NoKeyBoardActivity extends Activity {
	private Button SpeedUP_BTN;
	private Button SpeedAVG_BTN;
	private Button Stop_BTN;
	
	private Button CameraUP_BTN;
	private Button CameraDOWN_BTN;
	private Button CameraLEFT_BTN;
	private Button CameraRIGHT_BTN;
	
	private OfflineMapView mapView;
	private static VView videoView;
	
	private SocketManager socketManager = SocketManager.getInstance();
	
	private Thread initNOKeyBoardThread = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = ResultType.ReadyOK;
			MainctivityHandler_NoKeyBoard.sendMessage(message);
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (SettingData.CtrlMode == SettingData.KeyBoard) {
			Intent intent = new Intent(getApplicationContext(), 
					KeyBoradActivity.class);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nokeyboard);
		initNOKeyBoardViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nokeyboard, menu);
		return true;
	}

	
	private void initNOKeyBoardViews(){
		initNOKeyBoardThread.start();
		
		createGravitySensor();
		
		videoView = (VView)findViewById(R.id.videoView);
		mapView = (OfflineMapView)findViewById(R.id.mapView);
		SpeedAVG_BTN = (Button)findViewById(R.id.speedAVG_BTN);
		SpeedAVG_BTN.setOnTouchListener(new MyOnTouchListener(socketManager, 
				Action_Emotor.getInstance(), 
				Action_Steer.getInstance()));
		SpeedUP_BTN = (Button)findViewById(R.id.speedUP_BTN);
		SpeedUP_BTN.setOnTouchListener(new MyOnTouchListener(socketManager,
				Action_Emotor.getInstance(), 
				Action_Steer.getInstance()));
		Stop_BTN = (Button)findViewById(R.id.stop_BTN);
		Stop_BTN.setOnTouchListener(new MyOnTouchListener(socketManager, 
				Action_Emotor.getInstance(), 
				Action_Steer.getInstance()));
		
		CameraDOWN_BTN = (Button)findViewById(R.id.camera_DOWN_BTN);
		CameraDOWN_BTN.setOnTouchListener(new MyOnTouchListener(socketManager,
				Action_Emotor.getInstance(),
				Action_Steer.getInstance()));
		
		CameraLEFT_BTN = (Button)findViewById(R.id.camera_LEFT_BTN);
		CameraLEFT_BTN.setOnTouchListener(new MyOnTouchListener(socketManager,
				Action_Emotor.getInstance(),
				Action_Steer.getInstance()));
		CameraRIGHT_BTN = (Button)findViewById(R.id.camera_RIGHT_BTN);
		CameraRIGHT_BTN.setOnTouchListener(new MyOnTouchListener(socketManager, 
				Action_Emotor.getInstance(), 
				Action_Steer.getInstance()));
		
		CameraUP_BTN = (Button)findViewById(R.id.camera_UP_BTN);
		CameraUP_BTN.setOnTouchListener(new MyOnTouchListener(socketManager,
				Action_Emotor.getInstance(), 
				Action_Steer.getInstance()));
	}
	
	private class MyOnTouchListener implements OnTouchListener{
    	private SocketManager socketManager;
    	private Action_Emotor action_Emotor;
    	private Action_Steer action_Steer;
    	
    	public MyOnTouchListener(SocketManager socketManager,
    			Action_Emotor action_Emotor,
    			Action_Steer action_Steer){
    		this.socketManager = socketManager;
    		this.action_Emotor = action_Emotor;
    		this.action_Steer = action_Steer;
    	}
    	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (v.getId()) {
					case R.id.speedAVG_BTN:{
						break;
					}
					case R.id.speedUP_BTN:{
						break;
					}
					case R.id.stop_BTN:{
						break;
					}
					case R.id.camera_DOWN_BTN:{
						break;
					}
					case R.id.camera_LEFT_BTN:{
						break;
					}
					case R.id.camera_RIGHT_BTN:{
						break;
					}
					case R.id.camera_UP_BTN:{
						break;
					}
					
				}
			}else if (event.getAction() == KeyEvent.ACTION_UP) {
				switch (v.getId()) {
					case R.id.speedAVG_BTN:{
						break;
					}
					case R.id.speedUP_BTN:{
						break;
					}
					case R.id.stop_BTN:{
						break;
					}
					case R.id.camera_DOWN_BTN:{
						break;
					}
					case R.id.camera_LEFT_BTN:{
						break;
					}
					case R.id.camera_RIGHT_BTN:{
						break;
					}
					case R.id.camera_UP_BTN:{
						break;
					}
					
				}
			}
			return false;
		}
    }
	
	private Handler MainctivityHandler_NoKeyBoard = new Handler(){
		@Override
		public void handleMessage(Message message){
			switch (message.what) {
				case ResultType.Result_GPS:{
					Result_GPS result_GPS = Result_GPS.getInstance();
					mapView.setLocation(result_GPS.getLongtitude(), 
							result_GPS.getLatitude(), 
							18, 
							result_GPS.getSpeed(), 
							result_GPS.getHeight());
					break;
				}
				
				case ResultType.Result_OKCamera:{
					Result_OKCamera result_OKCamera = 
							Result_OKCamera.getInstance();
					socketManager.sendVideo(result_OKCamera.getFrameData());
					break;
				}
				
				case ResultType.Result_List:{
					Result_List result_List = 
							Result_List.getInstance();
					String[] lists = result_List.getParams();
					String list_Str = "";
					for (String list : lists) {
						list_Str += list + "\n";
					}
					Toast.makeText(getApplicationContext(), 
							list_Str, Toast.LENGTH_LONG).show();
					break;
				}
				
				case ResultType.Result_USBCamera:{
					Result_USBCamera result_USBCamera = 
							Result_USBCamera.getInstance();
					socketManager.sendVideo(result_USBCamera.getFrameData());
					break;
				}
				
				case ResultType.StartLink:{
					break;
				}
				
				case ResultType.ReadyOK:{
					videoView.playVideo();
					break;
				}
				default:{
					
					break;
				}
			}
		}
	};
	
	//--------------------------------------------------------------------------------
    //设置感应器
	private boolean isTurnLeft =false;	//小车是否右拐
	private int TurnD = 0;						//拐弯的角度
	 
    private SensorManager sensorManager;
    private Sensor sensor;
    private double x, y, z;		//从传感器中读取的数据
    private void createGravitySensor(){
    	// 得到当前手机传感器管理对象
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 加速重力感应对象
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 实例化一个监听器
        SensorEventListener lsn = new SensorEventListener() {
            // 实现接口的方法
            public void onSensorChanged(SensorEvent e) {
                // 得到各轴上的重力加速度
                x = e.values[SensorManager.DATA_X];
                y = e.values[SensorManager.DATA_Y];
                z = e.values[SensorManager.DATA_Z];
                
                //-----------------------------------------------------
                if (z > 8 && z < 0) {//判断手机的位置是否正确
					Toast.makeText(getApplicationContext(), "请保持手机的垂直放置", Toast.LENGTH_SHORT).show();
					TurnD = 0;
				}else {
					if (y < 1 && y > -1) {//直线运动
						TurnD = 0;
					}else {
						double degree = x/y;
						if (degree < 0) {
							isTurnLeft = true;
							if (degree >= -1) {//偏转角大于45度(左)
								TurnD = 100;
							}else {
								TurnD = (int) -(100/degree);
								if (TurnD < 16) {//确保指令传输的正确性
									TurnD = 16;
								}
							}
						}else if (degree > 0) {
							isTurnLeft = false;
							if (degree <= 1) {//偏转角大于45度(右)
								TurnD = 100;
							}else {
								TurnD = (int) (100/degree);
								if (TurnD < 16) {//确保指令传输的正确性
									TurnD = 16;
								}
							}
						}
					}
				}
                //判断方向
                if (isTurnLeft) {
					TurnD = -TurnD;
				}
                Action_Emotor action_Emotor = Action_Emotor.getInstance();
                action_Emotor.setX(TurnD);
                //Log.e(String.valueOf(TurnD), String.valueOf(isTurnLeft));
                //----------打印值
                //Toast.makeText(getApplicationContext(), String.valueOf(x) + ":" +String.valueOf(y) + ":" + String.valueOf(z), Toast.LENGTH_SHORT).show();
            }
            
            public void onAccuracyChanged(Sensor s, int accuracy) {}
        };
        // 注册listener，第三个参数是检测的精确度
        sensorManager.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
    }
    //-----------------------------------------------------------------------------
}
