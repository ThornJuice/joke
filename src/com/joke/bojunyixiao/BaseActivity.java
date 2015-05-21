package com.joke.bojunyixiao;

import com.joke.bojunyixiao.util.ActivityCollector;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity{
	private SharedPreferences sp;
	private boolean on_off;
	private boolean flag;
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      requestWindowFeature(Window.FEATURE_NO_TITLE);
			sp = getSharedPreferences("config", MODE_PRIVATE);
			on_off = sp.getBoolean("on_off", false);
			flag=on_off;
			if (on_off == false) {
				this.setTheme(R.style.MyLightTheme);
			}else{
				this.setTheme(R.style.MyNightTheme);
			}
	       ActivityCollector.addActivity(this);
	    }
	    @Override
	    protected void onDestroy(){
	        super.onDestroy();
	        ActivityCollector.removeActivity(this);
	    }
	    @Override
	    protected void onResume() {
	    	super.onResume();
			sp = getSharedPreferences("config", MODE_PRIVATE);
			on_off = sp.getBoolean("on_off", false);
			if(flag!=on_off){
				
				Intent intent=new Intent(getApplicationContext(),getClass());
				startActivity(intent);
				finish();
			}
	    }
}
