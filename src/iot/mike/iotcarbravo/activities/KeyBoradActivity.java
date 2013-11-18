package iot.mike.iotcarbravo.activities;

import h264.com.VView;
import iot.mike.iotcarbravo.data.Action_Emotor;
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
import iot.mike.iotcarbravo.net.NetUtil;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 使用键盘和头盔跟踪模块的活动
 * 
 * @author mikecoder
 * @date 2013-08-06
 */
public class KeyBoradActivity extends Activity {
	private SocketManager  socketManager = SocketManager.getInstance();
	
	private Dialog         dialog;	
	
	private ToggleButton   CarSTATE_TBTN;
	private Button         CameraRESER_BTN;
	
	private OfflineMapView mapView;
	private VView   videoView;
	
	private SensorManager sensorMgr;	// 感应器管理器
	private Sensor G_sensor, M_sensor;	// 得到方向感应器
	private float gx, gy, gz, ox;		// 定义各坐标轴上的重力加速度
	
	private float degree = 0;
	private float X_Degree, Y_Degree;	//两个值
	
	private volatile static float  pre_direction        = 0;
	private volatile static float  current_direction    = 0;
	
	private volatile float Ctrl_X = 0;	
	private volatile float Ctrl_Y = 0; 
	private volatile float Ctrl_Z = 0;//判断位
	
	
	private Timer addSpeedTimer = null;
	private class addSpeedTimerTask extends TimerTask{
		@Override
		public void run() {
			Action_Emotor.getInstance().addSpeed();
			try {
				socketManager.sendOrder(Action_Emotor.
						getInstance().getOrder());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Timer addTurnTimer = null;
	private class addTurnTimerTask extends TimerTask {
		@Override
		public void run() {
			Action_Emotor.getInstance().addTurn();
		}
		
	}
	
	private Timer reduceSpeedTimer = null;
	private class reduceSpeedTimerTask extends TimerTask{
		@Override
		public void run() {
			Action_Emotor.getInstance().reduceSpeed();
			try {
				socketManager.sendOrder(Action_Emotor.
						getInstance().getOrder());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Timer reduceTurnTimer = null;
	private class reduceTurnTimerTask extends TimerTask{
		@Override
		public void run() {
			Action_Emotor.getInstance().reduceTurn();
		}
	}
	
	private class GListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent e) {
            // 得到各轴上的重力加速度
            gx = e.values[SensorManager.DATA_X];
            gy = e.values[SensorManager.DATA_Y];
            gz = e.values[SensorManager.DATA_Z];
            dealDegree();
        }
        public void onAccuracyChanged(Sensor s, int accuracy) {}
	}
	
	private class MListener implements SensorEventListener {
        // 实现接口的方法
        public void onSensorChanged(SensorEvent event) {
             //右正左负
             if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
                 ox = event.values[SensorManager.DATA_X];
                 current_direction = ox;
                 if(pre_direction < 180){    //这个初始角的位置小于180度
                     if(ox > (pre_direction + 180) 
                             || ox < pre_direction){ 
                         //现在这个角度在pre_direction的左侧
                         if(ox >= pre_direction) //这个角度是没有过0度
                             degree = -(360 - ox + pre_direction);
                         else degree = -(pre_direction - ox); 
                         //是过了0度，在pre_direction和0之间
                     }else { //在pre_direction的右侧
                         degree = ox - pre_direction;
                     }
                 }else { //这个初始角的位置大于180度
                     if(ox > pre_direction - 180 
                             && ox < pre_direction){ 
                         //现在这个角度在pre_direction的左侧
                             degree = -(pre_direction - ox);
                     }else { //在pre_direction的右侧
                         if (ox >= pre_direction) 
                             //这个角度在pre_direction和0之间
                                 degree = ox - pre_direction;
                         else degree = 360 + ox - pre_direction; 
                         //这个角过了0度
                     }
                 }
                 Ctrl_X = degree;
             }
         }
        public void onAccuracyChanged(Sensor s, int accuracy) {}
	}
	
	private GListener gListener    = null;
	private MListener mListener    = null;
	
	@Override
	public void onDestroy() {
		try {
			Action_Emotor.getInstance().reset();
			Action_Steer.getInstance().reset();
			socketManager.sendOrder(Action_Emotor.
					getInstance().getOrder());
			socketManager.sendOrder(Action_Steer.
					getInstance().getOrder());
			super.onDestroy();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	//GPS信息
    private Control_GPS control_GPS = new Control_GPS();
    private Timer gPSDataTimer;
    private GPSTask gpsTask;
    private class GPSTask extends TimerTask {
        @Override
        public void run() {
            try{
                socketManager.sendOrder(control_GPS.getOrder());
            } catch(Exception e) {
                
            }
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		socketManager.startVideoServer();
		if (SettingData.CtrlMode == SettingData.KeyBoard) {
			setContentView(R.layout.activity_keyboard);
			initKeyBoardViews();
			socketManager.setKeyBoardActivityHandler(MainctivityHandler_KeyBoard);
		}else {
			Intent intent = new Intent(getApplicationContext(),
					NoKeyBoardActivity.class);
			Toast.makeText(getApplicationContext(), 
					"你选择了无外接键盘的操作方式", 
					Toast.LENGTH_LONG).show();
			startActivity(intent);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	//键盘数据
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(keyCode + ":", event.toString());
		
		//中间5键,复位
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_5) {
		    
		    pre_direction = current_direction;    //指向正前方
            
			Action_Steer.getInstance().reset();
			try {
				socketManager.sendOrder(Action_Steer.getInstance().getOrder());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}
		//上键,加速
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_8) {
			if (addSpeedTimer != null) {
				addSpeedTimer.cancel();
				addSpeedTimer = null;
			}
			addSpeedTimer = new Timer();
			addSpeedTimer.schedule(new addSpeedTimerTask(), 0, 500);
			return true;
		}
		//下键,减速
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_2) {
			if (reduceSpeedTimer != null) {
				reduceSpeedTimer.cancel();
				reduceSpeedTimer = null;
			}
			reduceSpeedTimer = new Timer();
			reduceSpeedTimer.schedule(new reduceSpeedTimerTask(), 0, 500);
			return true;
		}
		//左键,左拐
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_4) {
			if (reduceTurnTimer != null) {
				reduceTurnTimer.cancel();
				reduceTurnTimer = null;
			}
			reduceTurnTimer = new Timer();
			reduceTurnTimer.schedule(new reduceTurnTimerTask(), 0, 500);
			return true;
		}
		//右键,右拐
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_6) {
			if (addTurnTimer != null) {
				addTurnTimer.cancel();
				addTurnTimer = null;
			}
			addTurnTimer = new Timer();
			addTurnTimer.schedule(new addTurnTimerTask(), 0, 500);
			return true;
		}
		
		//切换视频 除号键
		if (keyCode == KeyEvent.KEYCODE_NUMPAD_DIVIDE) {
            if(Action_USBCamera.getInstance().getMode() == CameraMode.off) {
                Action_USBCamera.getInstance().setMode(CameraMode.on);
                Action_OKCamera.getInstance().setMode(CameraMode.off);
                try {
                    socketManager.sendOrder(Action_OKCamera.getInstance().getOrder());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    socketManager.sendOrder(Action_USBCamera.getInstance().getOrder());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
		
		// 摄像头复位 菜单建
		if (keyCode == KeyEvent.KEYCODE_MENU) {
		    Toast.makeText(getApplicationContext(), 
		            "复位", Toast.LENGTH_SHORT).show();
				
		    pre_direction = current_direction;
		    
		    Action_Steer action_Steer = Action_Steer.getInstance();
		    action_Steer.setA(0);action_Steer.setB(0);
		    try {
		        socketManager.sendOrder(action_Steer.getOrder());
		    } catch (JSONException e) {
		        e.printStackTrace();
		    }
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		//中间5键,刹车
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_5) {
			Action_Emotor.getInstance().reset();
			try {
				socketManager.sendOrder(Action_Emotor.getInstance().getOrder());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}
		//上键,加速
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_8) {
			if (addSpeedTimer != null) {
				addSpeedTimer.cancel();
				addSpeedTimer = null;
			}
			Action_Emotor.getInstance().reset();
			try {
				socketManager.sendOrder(Action_Emotor.getInstance().getOrder());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}
		//下键,减速
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_2) {
			if (reduceSpeedTimer != null) {
				reduceSpeedTimer.cancel();
				reduceSpeedTimer = null;
			}
			Action_Emotor.getInstance().reset();
			try {
				socketManager.sendOrder(Action_Emotor.getInstance().getOrder());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}
		//左键,左拐
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_4) {
			if (reduceTurnTimer != null) {
				reduceTurnTimer.cancel();
				reduceTurnTimer = null;
			}
			return true;
		}
		//右键,右拐
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				|| keyCode == KeyEvent.KEYCODE_NUMPAD_6) {
			if (addTurnTimer != null) {
				addTurnTimer.cancel();
				addTurnTimer = null;
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}	
	
	private Handler MainctivityHandler_KeyBoard = new Handler(){
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
					socketManager.sendVideo(
							result_OKCamera.getFrameData());
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
					socketManager.sendVideo(
							result_USBCamera.getFrameData());
					break;
				}
				
				case ResultType.StartLink:{
					break;
				}
				
				case ResultType.ReadyOK:{
					if (dialog != null) {
						dialog.dismiss();
						dialog.cancel();
						dialog = null;
					}
					videoView.playVideo();
					break;
				}
				case SocketManager.NETOK:{
				    socketManager.startVideoServer();
				    if (dialog != null) {
                        dialog.dismiss();
                        dialog.cancel();
                        dialog = null;
                    }
				    sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
                    videoView.playVideo();
                    createGravitySensor();
                    createMagneticSensor();
                    socketManager.sendOrder(NetUtil.GPS_SETTING);
                    
                    CarSTATE_TBTN.setChecked(true);
                    CarSTATE_TBTN.setEnabled(false);
                    
                    if (gPSDataTimer != null) {
                        gPSDataTimer.cancel();
                    }
                    gPSDataTimer = new Timer();
                    gpsTask = null;
                    gpsTask = new GPSTask();
                    gPSDataTimer.schedule(gpsTask, 50, 500);
                    break;
				}
				case SocketManager.NETERROR:{
				    Log.e("SocketManager", "Net Error");
				    if (dialog != null) {
                        dialog.dismiss();
                        dialog.cancel();
                        dialog = null;
                    }
				    CarSTATE_TBTN.setChecked(false);
				    CarSTATE_TBTN.setEnabled(true);
				    if (gListener != null) {
				        sensorMgr.unregisterListener(gListener);
                    }
				    if (mListener != null) {
				        sensorMgr.unregisterListener(mListener);
                    }
				    gListener = null;
				    mListener = null;
                    G_sensor = null;
                    M_sensor = null;
                    sensorMgr = null;
				    Toast.makeText(getApplicationContext(), 
				            "小车未连接...", 
				            Toast.LENGTH_SHORT).show();
				    socketManager.startVideoServer();
				}
				
				default:{
					break;
				}
			}
		}
	};
	
	private void initKeyBoardViews(){
	    CarSTATE_TBTN = (ToggleButton)findViewById(R.id.CarSTATE_TBTN);
	    CarSTATE_TBTN.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    socketManager = SocketManager.getInstance();
                    socketManager.startLink();
                    dialog = onCreateDialog(1);
                    dialog.show();
                }
            }
        });
		
		CameraRESER_BTN = (Button)findViewById(R.id.CameraRESET_BTN);
		CameraRESER_BTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Action_OKCamera action_OKCamera = Action_OKCamera.getInstance();
                Action_USBCamera action_USBCamera = Action_USBCamera.getInstance();
                if (action_USBCamera.getMode() == CameraMode.on) {
                    action_OKCamera.setMode(CameraMode.on);
                    action_USBCamera.setMode(CameraMode.off);
                }else {
                    action_OKCamera.setMode(CameraMode.off);
                    action_USBCamera.setMode(CameraMode.on);
                }
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
                Toast.makeText(getApplicationContext(), 
                        "切换摄像头成功！", 
                        Toast.LENGTH_SHORT).show();
            }
        });
		videoView = (VView)findViewById(R.id.video_VV);
		mapView = (OfflineMapView)findViewById(R.id.offlineMap_MAP);
		mapView.setLocation(120.638696551304, 31.304066035848, 18, 0, 0);
	}
	
	private void createGravitySensor(){
       // 加速重力感应对象
       G_sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
       // 实例化一个监听器
       if (gListener != null) {
           gListener = null;
       }
       gListener = new GListener();
       // 注册listener，第三个参数是检测的精确度
       sensorMgr.registerListener(gListener, 
               G_sensor, 
    		   SensorManager.SENSOR_DELAY_UI);
	}
	
	
	@SuppressWarnings("deprecation")
	private void createMagneticSensor(){
       // 加速方向感应对象
       M_sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION);
       // 实例化一个监听器
       if (mListener != null) {
        mListener = null;
       }
       mListener = new MListener();
       // 注册listener，第三个参数是检测的精确度
       sensorMgr.registerListener(mListener, 
               M_sensor, 
               SensorManager.SENSOR_DELAY_UI);
	}
	
	private void dealDegree(){
    	if(!(gx > 5)){
    		//Log.e("手机翻过", "不启动程序");
    	}else {//只有俯仰60度，左右60度
			Y_Degree = 90 - 10 * gz;
			if (Y_Degree > 150) 
				Y_Degree = 150;
			if (Y_Degree < 30) 
				Y_Degree = 30;
			X_Degree = 90 + 10 * gy;
			if (X_Degree > 150)
				X_Degree = 150;
			if (X_Degree < 30)
				X_Degree = 30;
			
            Ctrl_Y = Y_Degree;
            Ctrl_Z = X_Degree;
            
            if (Ctrl_Z < 105 && Ctrl_Z > 75) {
				//Log.e("D", "X:" + Ctrl_X + " Y:" + Ctrl_Y);
				Action_Steer action_Steer = Action_Steer.getInstance();
				action_Steer.setA((int)Ctrl_X);
				action_Steer.setB((int)Ctrl_Y - 90);
				try {
					socketManager.sendOrder(action_Steer.getOrder());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else {
				Toast.makeText(getApplicationContext(), "不要倾斜头部", Toast.LENGTH_LONG).show();
			}
		}
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            default: {         //有标题栏的进度对话框
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("正在连接.....");
                dialog.setMessage("Please wait a few seconds...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                return dialog;
            }
        }
    }
    
}
