package iot.mike.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	private static class MyOnKeyListener implements OnKeyListener{
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			Log.e(keyCode + ":", ":" + keyCode);
			return false;
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(keyCode + ":", event.toString());
		TextView textView = (TextView)findViewById(R.id.textView1);
		textView.setText(event.toString());
		return super.onKeyDown(keyCode, event);
	}
}
