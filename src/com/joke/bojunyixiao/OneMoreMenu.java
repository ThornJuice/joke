package com.joke.bojunyixiao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class OneMoreMenu extends BaseActivity implements OnClickListener{
	private RelativeLayout rl_setting;
	private RelativeLayout rl_about;
 @Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.one_more_menu_activity);
	
	rl_setting=(RelativeLayout)findViewById(R.id.rl_setting);
	rl_about=(RelativeLayout)findViewById(R.id.rl_about);
	rl_setting.setOnClickListener(this);
	rl_about.setOnClickListener(this);
}
@Override
public void onClick(View v) {
	Intent intent;
	switch (v.getId()) {
	case R.id.rl_setting:
		intent=new Intent(OneMoreMenu.this,SettingActivity.class);
		startActivity(intent);
		break;
	case R.id.rl_about:
		intent=new Intent();
		intent=new Intent(OneMoreMenu.this,AboutMe.class);
		startActivity(intent);
		break;
	}
}
}
