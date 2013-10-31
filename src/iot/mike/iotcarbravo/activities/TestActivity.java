package iot.mike.iotcarbravo.activities;

import iot.mike.iotcarbravo.data.Action_Emotor;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;

public class TestActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (addSpeedTimer == null || addSpeedTimerTask == null) {
				addSpeedTimer = new Timer();
				addSpeedTimerTask= new TimerTask() {
					@Override
					public void run() {
						Action_Emotor action_Emotor = Action_Emotor.getInstance();
						action_Emotor.addSpeed();
					}
				};
				addSpeedTimer.schedule(addSpeedTimerTask, 0, 500);
			}
			Log.e("d", keyCode + ":" + Action_Emotor.getInstance().getY());
		}
		return false;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		TextView textView = (TextView)findViewById(R.id.textView1);
		textView.setText(event.toString());
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (addSpeedTimer != null) {
				addSpeedTimerTask.cancel();
				addSpeedTimerTask = null;
				addSpeedTimer.cancel();
				addSpeedTimer = null;
			}
			Action_Emotor.getInstance().reset();
			Log.e("d", keyCode + ":" + Action_Emotor.getInstance().getY());
		}
		return false;
	}
	
	private Timer addSpeedTimer = null;
	private TimerTask addSpeedTimerTask = null;
	
	private Timer addTurnTimer = new Timer();
	private TimerTask addTurnTimerTask = new TimerTask() {
		@Override
		public void run() {
			Action_Emotor action_Emotor = Action_Emotor.getInstance();
			action_Emotor.addTurn();
		}
	};
	
	private Timer reduceSpeedTimer = new Timer();
	private TimerTask reduceSpeedTimerTask = new TimerTask() {
		@Override
		public void run() {
			Action_Emotor action_Emotor = Action_Emotor.getInstance();
			action_Emotor.reduceSpeed();
		}
	};
	
	private Timer reduceTurnTimer = new Timer();
	private TimerTask reduceTurnTimerTask = new TimerTask() {
		@Override
		public void run() {
			Action_Emotor action_Emotor = Action_Emotor.getInstance();
			action_Emotor.reduceTurn();
		}
	};
	
}
