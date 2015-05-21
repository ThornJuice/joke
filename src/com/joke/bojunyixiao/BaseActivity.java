package com.joke.bojunyixiao;

import com.joke.bojunyixiao.util.ActivityCollector;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{
	 protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	       ActivityCollector.addActivity(this);
	    }
	    @Override
	    protected void onDestroy(){
	        super.onDestroy();
	        ActivityCollector.removeActivity(this);
	    }
	    @Override
	    protected void onResume() {
	    	// TODO Auto-generated method stub
	    	super.onResume();
	    }
}
