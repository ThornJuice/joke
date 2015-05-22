package com.joke.bojunyixiao;

import java.io.File;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joke.bojunyixiao.util.ActivityCollector;
import com.joke.bojunyixiao.util.DataCleanManager;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_switch;
	private TextView tv_exit;
	private boolean on_off;
	private SharedPreferences sp;
	private TextView tv_version;
	private RelativeLayout rl_clearCache;
	private TextView tv_cacheSize;
	private long cacheSize;
	private static final String tag = "SettingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		on_off = sp.getBoolean("on_off", false);
		setContentView(R.layout.setting_activity);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText(getVersion());
		iv_switch = (ImageView) findViewById(R.id.iv_switch);
		tv_exit = (TextView) findViewById(R.id.tv_exit);
		tv_cacheSize = (TextView) findViewById(R.id.tv_cacheSize);
		try {
			cacheSize = DataCleanManager.getFolderSize(getCacheDir());
		} catch (Exception e) {

			e.printStackTrace();
		}
		tv_cacheSize.setText(DataCleanManager.getFormatSize(cacheSize));

		rl_clearCache = (RelativeLayout) findViewById(R.id.rl_clearCache);
		rl_clearCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteFilesByDirectory(getCacheDir());
				try {
					cacheSize = DataCleanManager.getFolderSize(getCacheDir());
					tv_cacheSize.setText(DataCleanManager.getFormatSize(cacheSize));
					Toast.makeText(getApplicationContext(), "刪除成功",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "臣妾做不到呀~~",
							Toast.LENGTH_SHORT).show();
				}	
			}
		});
		iv_switch.setOnClickListener(this);
		tv_exit.setOnClickListener(this);

		on_off = sp.getBoolean("on_off", false);
		if (on_off == false) {
			iv_switch.setImageResource(R.drawable.switch_off);
		} else {
			iv_switch.setImageResource(R.drawable.switch_on);
		}
	}
	private void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				if (item != null && item.exists() && item.isDirectory()) {
					deleteFilesByDirectory(item);
				} else {
					item.delete();
				}
			}
		}
	}
/*	private void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				if (item != null && item.exists() && item.isDirectory()) {
					for (File file : item.listFiles()) {
						file.delete();
					}
				} else {
					item.delete();
				}
			}
		}
	}*/

	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		PackageManager pm = this.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(),
					0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();

			return "未知";
		}
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
				Intent intent = new Intent(SettingActivity.this,
						SettingActivity.class);
				startActivity(intent);
			} else {
				iv_switch.setImageResource(R.drawable.switch_off);
				edit.putBoolean("on_off", false);
				edit.commit();
				finish();
				Intent intent = new Intent(SettingActivity.this,
						SettingActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.tv_exit:
			ActivityCollector.finishAll();
			break;
		}
	}
}
