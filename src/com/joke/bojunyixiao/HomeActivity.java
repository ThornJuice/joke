package com.joke.bojunyixiao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.joke.bojunyixiao.adapter.MyPicJokeAdapater;
import com.joke.bojunyixiao.adapter.MyTextJokeAdapter;
import com.joke.bojunyixiao.entity.PicJoke;
import com.joke.bojunyixiao.entity.TextJoke;
import com.joke.bojunyixiao.util.ActivityCollector;

public class HomeActivity extends BaseActivity implements OnClickListener {
	private MyTextJokeAdapter textJokeAdapter;
	private MyPicJokeAdapater picJokeAdapter;
	private ViewPager vp = null;
	private List<View> views = null;
	private TextView tvTag1;
	private TextView tvTag2;
	private Timer timer;
	private TimerTask timerTask;
	private boolean isExit = false;
	private PullToRefreshListView ptrlvPicjokes;
	private PullToRefreshListView ptrlvAlljokes;
	private List<TextJoke> jokeLists;
	private List<TextJoke> refreshJokes;
	private List<PicJoke> PicJokes;
	private List<PicJoke> refreshPicJokes;
	private static int page = 1;
	private static int picPage = 1;
	boolean isRefresh = true;
	private SharedPreferences sp;
	private boolean on_off;
	private boolean flag;
	private ImageButton ib_one_more;
	protected static final String tag = "HomeActivity";

	@Override
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
		setContentView(R.layout.activity_home);
		timer=new Timer();
		ib_one_more=(ImageButton) findViewById(R.id.ib_one_more);
		ib_one_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(HomeActivity.this,OneMoreMenu.class);
				startActivity(intent);
			}});
		
/*		// 设置抽屉菜单
				slidingMenu = new SlidingMenu(this);
				slidingMenu.setMode(SlidingMenu.LEFT);
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 触摸边界拖出菜单
				slidingMenu.setMenu(R.layout.slidingmenu_left);
				slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
				// 将抽屉菜单与主页面关联起来
				slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);*/
		tvTag1 = (TextView) findViewById(R.id.tvTag1);
		tvTag2 = (TextView) findViewById(R.id.tvTag2);

		tvTag1.setOnClickListener(this);
		tvTag2.setOnClickListener(this);

		views = initView();
		PicJokes = new ArrayList<PicJoke>();
		jokeLists = new ArrayList<TextJoke>();
		vp = (ViewPager) findViewById(R.id.vpViewPager1);
		vp.setAdapter(new MyViewPager(views));

		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				switch (arg0) {
				case 0:
					tvTag1.setTextColor(getResources().getColor(R.color.blue));
					tvTag2.setTextColor(Color.BLACK);
					
					break;
				case 1:
					tvTag2.setTextColor(getResources().getColor(R.color.blue));
					tvTag1.setTextColor(Color.BLACK);
					
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		MyViewPager adapter = (MyViewPager) vp.getAdapter();
		View v1 = adapter.getItemAtPosition(0);
		View v2 = adapter.getItemAtPosition(1);
		// 加载网络数据
		getJsonByVolly(HomeActivity.this);
		getPicByVolley(HomeActivity.this);
		textJokeAdapter = new MyTextJokeAdapter(this, jokeLists);
		picJokeAdapter = new MyPicJokeAdapater(this, PicJokes);
		// 初始化文本笑话列表
		ptrlvAlljokes = (PullToRefreshListView) v1
				.findViewById(R.id.ptrlvAllJokes);
		ptrlvAlljokes.setMode(Mode.PULL_FROM_START);
		ptrlvAlljokes.setAdapter(textJokeAdapter);
	
		ptrlvAlljokes.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				int flags = DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL;

				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						flags);
				// 更新最后刷新时间
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
						"上次更新时间" + label);
				page = 1;
				// 执行加载更多数据任务.
				upDateJsonByVolly(HomeActivity.this);

			}

		});
		// 监听滑动到底部的事件
		ptrlvAlljokes
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(getApplicationContext(), "正在加载更多",
								Toast.LENGTH_SHORT).show();
						// 执行加载更多数据任务.
						page++;
						upDateJsonByVolly(HomeActivity.this);
					}
				});
		// 初始化图片笑话列表
		ptrlvPicjokes = (PullToRefreshListView) v2
				.findViewById(R.id.ptrlvPicJokes);
		ptrlvPicjokes.setMode(Mode.PULL_FROM_START);
		ptrlvPicjokes.setAdapter(picJokeAdapter);
		
		ptrlvPicjokes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
			String imgUrl=PicJokes.get(position-1).getPicUrl();
			Intent intent=new Intent(HomeActivity.this,FullPicActivity.class);
		   intent.putExtra("imgUrl",imgUrl);
			startActivity(intent);
			}
		});
		ptrlvPicjokes.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				picPage = 1;
				upDatePicJokeByVolley(HomeActivity.this);
			}
		});
		ptrlvPicjokes
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(getApplicationContext(), "正在加载更多",
								Toast.LENGTH_SHORT).show();
						// 执行加载更多数据任务.
						picPage++;
						upDatePicJokeByVolley(HomeActivity.this);
					}
				});
	}
	// 从服务器取图片json数据
	public void getPicByVolley(Context context) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String prefixPic=getString(R.string.pic_url);
		//String url = prefixPic+1 ;
		String url=getString(R.string.test_pic);
		MyJsonObjectRequest jor = new MyJsonObjectRequest(0, url,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject json) {
						Log.e(tag, json.toString());

						try {
							JSONObject result = json.getJSONObject("result");
							JSONArray items = result.getJSONArray("data");
							for (int i = 0; i < items.length(); i++) {
								JSONObject item = items.getJSONObject(i);
								PicJoke joke = new PicJoke();
								joke.setUpDatetime(item.optString("updatetime"));
								joke.setTitle(item.optString("content"));
								joke.setPicUrl(item.optString("url"));
								PicJokes.add(joke);
							}

						} catch (JSONException e) {

							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "数据加载异常!",
								Toast.LENGTH_SHORT).show();
					}
				});

		requestQueue.add(jor);
	}

	// 更新图片
	public void upDatePicJokeByVolley(Context context) {
		refreshPicJokes = new ArrayList<PicJoke>();
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String prefixPic=getString(R.string.pic_url);
		String url = prefixPic+page ;
		MyJsonObjectRequest jor = new MyJsonObjectRequest(0, url,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject json) {
						Log.e(tag, json.toString());

						try {
							JSONObject result = json.getJSONObject("result");
							JSONArray items = result.getJSONArray("data");
							for (int i = 0; i < items.length(); i++) {
								JSONObject item = items.getJSONObject(i);
								PicJoke joke = new PicJoke();
								joke.setUpDatetime(item.optString("updatetime"));
								joke.setTitle(item.optString("content"));
								joke.setPicUrl(item.optString("url"));
								refreshPicJokes.add(joke);
							}
							if (picPage == 1) {
								PicJokes.clear();
							}
							PicJokes.addAll(refreshPicJokes);
							ptrlvPicjokes.onRefreshComplete();
							picJokeAdapter.notifyDataSetChanged();

						} catch (JSONException e) {

							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "数据加载异常!",
								Toast.LENGTH_SHORT).show();
					}
				});

		requestQueue.add(jor);

	}

	// 从服务器取文本json数据
	public void getJsonByVolly(Context context) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String prefixText=getString(R.string.text_url);
		//String url = prefixText+1 ;
		String url=getString(R.string.test_text);
		MyJsonObjectRequest jor = new MyJsonObjectRequest(0, url,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject json) {
						// Log.e(tag, json.toString());
						try {
							JSONObject result = json.getJSONObject("result");
							JSONArray items = result.getJSONArray("data");
							for (int i = 0; i < items.length(); i++) {
								JSONObject item = items.getJSONObject(i);
								TextJoke joke = new TextJoke();
								joke.setUpDatetime(item.optString("updatetime"));
								joke.setText(item.optString("content"));
								jokeLists.add(joke);
							}

						} catch (JSONException e) {

							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(tag,
								"error.......................................");
					}
				});

		requestQueue.add(jor);
	}

	// 从服务器更新文本json数据
	public void upDateJsonByVolly(Context context) {
		refreshJokes = new ArrayList<TextJoke>();
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String prefixText=getString(R.string.text_url);
		String url = prefixText+page ;
		MyJsonObjectRequest jor = new MyJsonObjectRequest(0, url,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject json) {
						try {
							JSONObject result = json.getJSONObject("result");
							JSONArray items = result.getJSONArray("data");
							for (int i = 0; i < items.length(); i++) {
								JSONObject item = items.getJSONObject(i);
								TextJoke joke = new TextJoke();
								joke.setUpDatetime(item.optString("updatetime"));
								joke.setText(item.optString("content"));
								refreshJokes.add(joke);
							}
							if (page == 1) {
								jokeLists.clear();
							}
							jokeLists.addAll(refreshJokes);
							ptrlvAlljokes.onRefreshComplete();
							textJokeAdapter.notifyDataSetChanged();

						} catch (JSONException e) {

							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(tag,
								"error.......................................");
					}
				});

		requestQueue.add(jor);
	}

	public class MyJsonObjectRequest extends Request<JSONObject> {
		private Listener<JSONObject> mlistener;

		public MyJsonObjectRequest(int method, String url,
				Listener<JSONObject> mlistener, ErrorListener listener) {
			super(method, url, listener);
			this.mlistener = mlistener;
		}

		@Override
		protected Response<JSONObject> parseNetworkResponse(
				NetworkResponse response) {
			try {

				String jsonString = new String(response.data, "utf-8");
				return Response.success(new JSONObject(jsonString),
						HttpHeaderParser.parseCacheHeaders(response));

			} catch (UnsupportedEncodingException e) {
				return Response.error(new ParseError(e));
			} catch (JSONException je) {
				return Response.error(new ParseError(je));
			}

		}

		@Override
		protected void deliverResponse(JSONObject response) {
			mlistener.onResponse(response);
		}
	}

	public List<View> initView() {

		views = new ArrayList<View>();
		views.add(LayoutInflater.from(this).inflate(R.layout.all_jokes, null));
		views.add(LayoutInflater.from(this).inflate(R.layout.pic_jokes, null));
		return views;
	}

	class MyViewPager extends PagerAdapter {
		private List<View> views;

		public MyViewPager(List<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(views.get(position));

			return views.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public View getItemAtPosition(int position) {
			return views.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvTag1:
			vp.setCurrentItem(0);
			tvTag1.setTextColor(getResources().getColor(R.color.blue));
			tvTag2.setTextColor(Color.BLACK);
			break;
		case R.id.tvTag2:
			vp.setCurrentItem(1);
			tvTag2.setTextColor(getResources().getColor(R.color.blue));
			tvTag1.setTextColor(Color.BLACK);
			break;
		}
	}
	@Override
	protected void onResume() {
		
		super.onResume();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		on_off = sp.getBoolean("on_off", false);
		if(flag!=on_off){
			finish();
			Intent intent=new Intent(HomeActivity.this,HomeActivity.class);
			startActivity(intent);
		}	
	}
	@Override
	public void onBackPressed() {
		
		if (isExit) {
			ActivityCollector.finishAll();
		} else {
			isExit = true;
			Toast.makeText(HomeActivity.this, "再按一次退出", Toast.LENGTH_SHORT)
					.show();
			timerTask = new TimerTask() {

				@Override
				public void run() {
					isExit = false;
				}
			};
			
			timer.schedule(timerTask, 2000);
		}
	
	}
}
