package com.joke.bojunyixiao;


import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Splash extends BaseActivity{
	private TimerTask task;
	private Timer timer;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {	
	 	super.onCreate(savedInstanceState);
	 	requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	setContentView(R.layout.about_me);
	 	
	 	loadMainUi();
	 }
		private void loadMainUi() {
			timer=new Timer();
			task=new TimerTask() {
				@Override
				public void run() {
					Intent intent = new Intent(Splash.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}
			};
			timer.schedule(task, 2000);	
		}
}
