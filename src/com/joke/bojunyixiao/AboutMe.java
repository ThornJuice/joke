package com.joke.bojunyixiao;

import android.os.Bundle;
import android.view.Window;


public class AboutMe extends BaseActivity{

 @Override
protected void onCreate(Bundle savedInstanceState) {	
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.about_me);
}
}