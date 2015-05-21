package com.joke.bojunyixiao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joke.bojunyixiao.util.ActivityCollector;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_switch;
	private TextView tv_exit;
	private boolean on_off;
	private SharedPreferences sp;
	private TextView tv_version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		on_off = sp.getBoolean("on_off", false);
		
		if (on_off == false) {
			this.setTheme(R.style.MyLightTheme);
		}else{
			this.setTheme(R.style.MyNightTheme);
		}

		setContentView(R.layout.setting_activity);	
		tv_version=(TextView)findViewById(R.id.tv_version);
		tv_version.setText(getVersion());
		iv_switch = (ImageView) findViewById(R.id.iv_switch);
		tv_exit = (TextView) findViewById(R.id.tv_exit);
		
		iv_switch.setOnClickListener(this);
		tv_exit.setOnClickListener(this);
		
		on_off = sp.getBoolean("on_off", false);
		if (on_off == false) {
			iv_switch.setImageResource(R.drawable.switch_off);
		} else {
			iv_switch.setImageResource(R.drawable.switch_on);
		}
	}

	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		PackageManager pm = this.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(),0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			
			return "未知";
		}
	}
@Override
protected void onResume() {
	
	super.onResume();
}
	@Override
	public void onClick(View v) {

		Editor edit = sp.edit();
		on_off = sp.getBoolean("on_off", false);
		switch (v.getId()) {
		case R.id.iv_switch:
			if (on_off == false) {
				iv_switch.setImageResource(R.drawable.switch_on);
				edit.putBoolean("on_off", true);
				edit.commit();
				finish();
				Intent intent=new Intent(SettingActivity.this,SettingActivity.class);
				startActivity(intent);
			} else {
				iv_switch.setImageResource(R.drawable.switch_off);
				edit.putBoolean("on_off", false);
				edit.commit();
				finish();
				Intent intent=new Intent(SettingActivity.this,SettingActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.tv_exit:
			ActivityCollector.finishAll();
			break;
		}
	}
}
