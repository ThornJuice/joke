package com.joke.bojunyixiao;

import java.util.HashMap;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

public class FullPicActivity extends Activity {
	private static final String tag = "DialogActivity";
	PhotoView iv_showPic;
	ImageLoader imageLoader;
	ImageListener imageListener;
	private PopupWindow popupWindow;
	View contentView;
	HashMap<String, Bitmap> bms;
	private long firstClickedTime;
	private PhotoViewAttacher attacher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		
		
		bms = new HashMap<String, Bitmap>();
		iv_showPic = (PhotoView) findViewById(R.id.iv_showPic);
		attacher=new PhotoViewAttacher(iv_showPic);
		RequestQueue requestQueue = Volley
				.newRequestQueue(getApplicationContext());
		BitmapCache bmc = new BitmapCache();
		Intent intent = getIntent();
		String imgUrl = intent.getStringExtra("imgUrl");
		imageLoader = new ImageLoader(requestQueue, bmc);
		imageListener=imageLoader.getImageListener(iv_showPic, R.drawable.temp, R.drawable.temp);
		imageLoader.get(imgUrl, imageListener,200,200);
	
		attacher.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				finish();
			}
		});
		}
	private boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public class BitmapCache implements ImageCache {
		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			int maxSize = 10 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {

					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			// Log.d(tag, url + "%%%%%%%%%%%%%%%%%%%%%%%%%%");
			bms.put(url, mCache.get(url));
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
			bms.put(url, mCache.get(url));
			// Log.d(tag, url + "@@@@@@@@@@@@@@@@@@@@@");
		}
	}
}
