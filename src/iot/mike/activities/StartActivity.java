package iot.mike.activities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
}
