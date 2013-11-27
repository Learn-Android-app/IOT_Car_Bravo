package iot.mike.iotcarbravo.activities;

import h264.com.VView;
import iot.mike.iotcarbravo.data.Action_Emotor;
import iot.mike.iotcarbravo.data.Action_List;
import iot.mike.iotcarbravo.data.Action_OKCamera;
import iot.mike.iotcarbravo.data.Action_Steer;
import iot.mike.iotcarbravo.data.Action_USBCamera;
import iot.mike.iotcarbravo.data.CameraMode;
import iot.mike.iotcarbravo.data.Control_GPS;
import iot.mike.iotcarbravo.data.ResultType;
import iot.mike.iotcarbravo.data.Result_GPS;
import iot.mike.iotcarbravo.data.Result_List;
import iot.mike.iotcarbravo.data.Result_OKCamera;
import iot.mike.iotcarbravo.data.Result_USBCamera;
import iot.mike.iotcarbravo.mapview.OfflineMapView;
import iot.mike.iotcarbravo.net.SocketManager;
import iot.mike.iotcarbravo.setting.SettingData;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 未使用外界键盘的界面
 * 
 * @author mike
 * @date 2013-11-18
 */
public class NoKeyBoardActivity extends Activity {
	private Button SpeedUP_BTN;
	private Button SpeedAVG_BTN;
	@SuppressWarnings("unused")
    private Button Stop_BTN;
	private Button Slow_BTN;
	
	private boolean isFront = true;
	private Button CameraSELECT_BTN;
	private Button CameraRESER_BTN;
	private Button CameraUP_BTN;
	private Button CameraDOWN_BTN;
	private Button CameraLEFT_BTN;
	private Button CameraRIGHT_BTN;
	
	private ToggleButton startLink_TBN;
	
	private OfflineMapView mapView;
	private static VView videoView;
	
	private SocketManager socketManager = SocketManager.getInstance();
	private Glistener glistener;
	private Dialog dialog; 
	
	private Handler MainctivityHandler_NoKeyBoard = new Handler(){
        @Override
        public void handleMessage(Message message){
            try{
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
                            "小车连接成功！", Toast.LENGTH_LONG).show();
                    break;
                }
                
                case ResultType.Result_USBCamera:{
                    Result_USBCamera result_USBCamera = 
                            Result_USBCamera.getInstance();
                    socketManager.sendVideo(result_USBCamera.getFrameData());
                    break;
                }
                
                case ResultType.StartLink:{
                    socketManager.setKeyBoardActivityHandler(MainctivityHandler_NoKeyBoard);
                    socketManager.startLink();
                    break;
                }
                
                case ResultType.ReadyOK:{
                    break;
                }
                
                case SocketManager.NETERROR:{
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog.cancel();
                        dialog = null;
                    }
                    Toast.makeText(getApplicationContext(), "小车未能连接！", Toast.LENGTH_SHORT).show();
                    startLink_TBN.setChecked(false);
                    startLink_TBN.setEnabled(true);
                    break;
                }
                
                case SocketManager.NETOK:{
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog.cancel();
                        dialog = null;
                    }
                    try {
                        Action_USBCamera.getInstance().setMode(CameraMode.on);
                        socketManager.sendOrder(Action_USBCamera.getInstance().getOrder());
                        socketManager.sendOrder(Action_List.getInstance().getOrder());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (gPSDataTimer != null) {
                        gPSDataTimer.cancel();
                    }
                    gPSDataTimer = new Timer();
                    gpsTask = null;
                    gpsTask = new GPSTask();
                    gPSDataTimer.schedule(gpsTask, 50, 500);
                    
                    videoView.playVideo();
                    startLink_TBN.setChecked(true);
                    startLink_TBN.setEnabled(false);
                    break;
                }
                default:{
                    
                    break;
                }
            }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
	
	//GPS信息
	private Control_GPS control_GPS = new Control_GPS();
	private Timer gPSDataTimer;
	private GPSTask gpsTask;
	private class GPSTask extends TimerTask {
        @Override
        public void run() {
            try{
                //socketManager.sendOrder(control_GPS.getOrder());
            } catch(Exception e) {
                
            }
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (SettingData.CtrlMode == SettingData.KeyBoard) {
			Intent intent = new Intent(getApplicationContext(), 
					KeyBoradActivity.class);
			startActivity(intent);
			finish();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nokeyboard);
		initNOKeyBoardViews();
		socketManager = SocketManager.getInstance();
		socketManager.setKeyBoardActivityHandler(MainctivityHandler_NoKeyBoard);
        socketManager.startVideoServer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	
	private void initNOKeyBoardViews(){
		createGravitySensor();
		
		startLink_TBN = (ToggleButton)findViewById(R.id.carState_TBTN);
		startLink_TBN.setChecked(false);
		startLink_TBN.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    socketManager = SocketManager.getInstance();
                    socketManager.startLink();
                    dialog = onCreateDialog(1);
                    if (dialog == null) {
                        dialog.show();
                    }else {
                        Toast.makeText(getApplicationContext(), "正在连接...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
		
		videoView = (VView)findViewById(R.id.videoView);
		
		mapView = (OfflineMapView)findViewById(R.id.mapView);
		mapView.setLocation(120.638696551304, 31.304066035848, 18, 0, 0);
		SpeedAVG_BTN = (Button)findViewById(R.id.speedAVG_BTN);
		SpeedAVG_BTN.setOnTouchListener(new MyOnTouchListener(socketManager, 
				Action_Emotor.getInstance(), 
				Action_Steer.getInstance()));
		SpeedUP_BTN = (Button)findViewById(R.id.speedUP_BTN);
		SpeedUP_BTN.setOnTouchListener(new MyOnTouchListener(socketManager,
				Action_Emotor.getInstance(), 
				Action_Steer.getInstance()));
		//Stop_BTN = (Button)findViewById(R.id.stop_BTN);
		//Stop_BTN.setOnTouchListener(new MyOnTouchListener(socketManager, 
		//		Action_Emotor.getInstance(), 
		//		Action_Steer.getInstance()));
		
		Slow_BTN = (Button)findViewById(R.id.speedDOWN_BTN);
		Slow_BTN.setOnTouchListener(new MyOnTouchListener(socketManager, 
		        Action_Emotor.getInstance(), 
		        Action_Steer.getInstance()));
		
		CameraRESER_BTN = (Button)findViewById(R.id.camera_RESET_BTN);
		CameraRESER_BTN.setOnTouchListener(new MyOnTouchListener(socketManager, 
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
		
		CameraSELECT_BTN = (Button)findViewById(R.id.camera_SELECT_BTN);
		CameraSELECT_BTN.setOnTouchListener(new MyOnTouchListener(socketManager, 
		        Action_Emotor.getInstance(), 
		        Action_Steer.getInstance()));
		
	}
	
	private class MyOnTouchListener implements OnTouchListener{
    	private SocketManager socketManager;
    	
    	public MyOnTouchListener(SocketManager socketManager,
    			Action_Emotor action_Emotor,
    			Action_Steer action_Steer){
    		this.socketManager = socketManager;
    	}
    	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (v.getId()) {
					case R.id.speedAVG_BTN:{
						avgspeedTimer = null;
						sendOrderTimer = null;
						
						avgspeedTimer = new Timer();
						sendOrderTimer = new Timer();
						
						avgSpeedTimerTask aSpeedTimerTask = new avgSpeedTimerTask();
						sendOrderTimerTask sendTask = new sendOrderTimerTask();
						
						avgspeedTimer.schedule(aSpeedTimerTask, 0, 50);
						sendOrderTimer.schedule(sendTask, 0, 500);
						break;
					}
					case R.id.speedUP_BTN:{
						addspeedTimer = null;
						sendOrderTimer = null;
						
						addspeedTimer = new Timer();
						sendOrderTimer = new Timer();
						
						addSpeedTimerTask aSpeedTimerTask = new addSpeedTimerTask();
						sendOrderTimerTask sOrderTimerTask = new sendOrderTimerTask();
						
						addspeedTimer.schedule(aSpeedTimerTask, 0, 50);
						sendOrderTimer.schedule(sOrderTimerTask, 0, 500);
						break;
					}
					case R.id.speedDOWN_BTN: {
					    slowSpeedTimer = null;
                        sendOrderTimer = null;
                        
                        slowSpeedTimer = new Timer();
                        sendOrderTimer = new Timer();
                        
                        slowSpeedTimerTask sSpeedTimerTask = new slowSpeedTimerTask();
                        sendOrderTimerTask sOrderTimerTask = new sendOrderTimerTask();
                        
                        slowSpeedTimer.schedule(sSpeedTimerTask, 0, 50);
                        sendOrderTimer.schedule(sOrderTimerTask, 0, 500);
					    break;
					}
					case R.id.camera_DOWN_BTN:{
						Action_Steer action_Steer = Action_Steer.getInstance();
						action_Steer.redB();
						socketManager = SocketManager.getInstance();
						
						try {
	                        socketManager.sendOrder(action_Steer.getOrder());
                        } catch (JSONException e) {
	                        e.printStackTrace();
                        }
						break;
					}
					case R.id.camera_LEFT_BTN:{
						Action_Steer action_Steer = Action_Steer.getInstance();
						action_Steer.redA();
						socketManager = SocketManager.getInstance();
						
						try {
	                        socketManager.sendOrder(action_Steer.getOrder());
                        } catch (JSONException e) {
	                        e.printStackTrace();
                        }
						break;
					}
					case R.id.camera_RIGHT_BTN:{
						Action_Steer action_Steer = Action_Steer.getInstance();
						action_Steer.addA();
						socketManager = SocketManager.getInstance();
						
						try {
	                        socketManager.sendOrder(action_Steer.getOrder());
                        } catch (JSONException e) {
	                        e.printStackTrace();
                        }
						break;
					}
					case R.id.camera_UP_BTN:{
						Action_Steer action_Steer = Action_Steer.getInstance();
						action_Steer.addB();
						socketManager = SocketManager.getInstance();
						
						try {
	                        socketManager.sendOrder(action_Steer.getOrder());
                        } catch (JSONException e) {
	                        e.printStackTrace();
                        }
						break;
					}
					case R.id.camera_RESET_BTN:{
					    Action_Steer action_Steer = Action_Steer.getInstance();
					    action_Steer.reset();
					    try {
                            socketManager.sendOrder(action_Steer.getOrder());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
					    break;
					}
					case R.id.camera_SELECT_BTN:{
					    if (isFront) {
                            Action_USBCamera action_USBCamera =
                                    Action_USBCamera.getInstance();
                            action_USBCamera.setMode(CameraMode.off);
                            
                            Action_OKCamera action_OKCamera =
                                    Action_OKCamera.getInstance();
                            action_OKCamera.setMode(CameraMode.on);
                            
                            try {
                                socketManager.sendOrder(action_USBCamera.getOrder());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                socketManager.sendOrder(action_OKCamera.getOrder());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            isFront = false;
                        }else {
                            Action_USBCamera action_USBCamera =
                                    Action_USBCamera.getInstance();
                            action_USBCamera.setMode(CameraMode.on);
                            
                            Action_OKCamera action_OKCamera =
                                    Action_OKCamera.getInstance();
                            action_OKCamera.setMode(CameraMode.off);
                            
                            try {
                                socketManager.sendOrder(action_OKCamera.getOrder());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                socketManager.sendOrder(action_USBCamera.getOrder());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            isFront = false;
                            isFront = true;
                        }
					    break;
					}
				}
			}else if (event.getAction() == KeyEvent.ACTION_UP) {
			    Action_Emotor.getInstance().reset();
			    socketManager = SocketManager.getInstance();
			    try {
                    socketManager.sendOrder(Action_Emotor.getInstance().getOrder());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
				if (sendOrderTimer != null) {
					sendOrderTimer.cancel();
                }
				sendOrderTimer = null;
				switch (v.getId()) {
					case R.id.speedAVG_BTN:{
						if (avgspeedTimer != null) {
							avgspeedTimer.cancel();
                        }
						avgspeedTimer = null;
						break;
					}
					case R.id.speedUP_BTN:{
						if (addspeedTimer != null) {
	                        addspeedTimer.cancel();
                        }
						addspeedTimer = null;
						break;
					}
					
					case R.id.speedDOWN_BTN:{
					    if (slowSpeedTimer != null) {
                            slowSpeedTimer.cancel();
                        }
					    slowSpeedTimer = null;
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
        glistener = new Glistener();
        // 注册listener，第三个参数是检测的精确度
        sensorManager.registerListener(glistener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }
    //-----------------------------------------------------------------------------
    
    //发送命令区域
    //--------------------------------------------------------
    private Timer slowSpeedTimer = new Timer();
    private class slowSpeedTimerTask extends TimerTask{
    	@Override
		public void run() {
			Action_Emotor action_Emotor = Action_Emotor.getInstance();
			action_Emotor.reduceSpeed();
		}
	};
    //--------------------------------------------------------
    
    //加减速度区域
    //--------------------------------------------------------
    private Timer addspeedTimer = new Timer();
    private class addSpeedTimerTask extends TimerTask {//用来加速度的任务
    	@Override
		public void run() {
			Action_Emotor action_Emotor = Action_Emotor.getInstance();
			action_Emotor.addSpeed();
    	};
    }
	//--------------------------------------------------------
    
    //均匀速度区域
    //--------------------------------------------------------
    private Timer avgspeedTimer = new Timer();
    private class avgSpeedTimerTask extends TimerTask {//用来加速度的任务
    	@Override
		public void run() {
			Action_Emotor action_Emotor = Action_Emotor.getInstance();
			action_Emotor.setY(50);
			// DO nothing
    	};
    }
	//--------------------------------------------------------
    
    //发送指令区域
    //--------------------------------------------------------
    private Timer sendOrderTimer = new Timer();
    private class sendOrderTimerTask extends TimerTask {//用来加速度的任务
    	@Override
		public void run() {
    		socketManager = SocketManager.getInstance();
    		try {
				socketManager.sendOrder(Action_Emotor.getInstance().getOrder());
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	};
    }
	//--------------------------------------------------------
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            default: {         //有标题栏的进度对话框
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("正在连接.....");
                dialog.setMessage("Please wait for a few seconds...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                return dialog;
            }
        }
    }
    
    @Override
    public void onDestroy(){
        if (glistener != null) {
            sensorManager.unregisterListener(glistener);
            glistener = null;
        }
        if (socketManager != null) {
             socketManager.close();
        }
        socketManager = null;
        this.finish();
        MainctivityHandler_NoKeyBoard = null;
        super.onDestroy();
    }
    
    private class Glistener implements SensorEventListener {
        // 实现接口的方法
        @SuppressWarnings("deprecation")
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
                            TurnD = (int)-(100/degree);
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
            //----------打印值
            //Toast.makeText(getApplicationContext(), String.valueOf(x) + ":" +String.valueOf(y) + ":" + String.valueOf(z), Toast.LENGTH_SHORT).show();
        }
            
        public void onAccuracyChanged(Sensor s, int accuracy) {}
    }
}
