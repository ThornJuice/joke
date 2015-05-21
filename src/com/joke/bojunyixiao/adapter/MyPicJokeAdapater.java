package com.joke.bojunyixiao.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.joke.bojunyixiao.R;
import com.joke.bojunyixiao.entity.PicJoke;

public class MyPicJokeAdapater extends BaseAdapter{
 private List<PicJoke> jokes;
 private Context context;
private ImageLoader imageLoader;
public MyPicJokeAdapater(Context context,List<PicJoke> jokes) {
	this.context=context;
	this.jokes=jokes;
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jokes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jokes.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_picjoke, null);
			holder = new ViewHolder();
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.ivPicJoke=(NetworkImageView)convertView.findViewById(R.id.ivPicJoke);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvDate.setText(jokes.get(position).getUpDatetime());	
		holder.tvTitle.setText(jokes.get(position).getTitle());
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		imageLoader=new ImageLoader(requestQueue, new BitmapCache());
		holder.ivPicJoke.setImageUrl(jokes.get(position).getPicUrl(),imageLoader);
		return convertView;
		
	}
	static class ViewHolder {
		TextView tvDate;
		TextView tvTitle;
		NetworkImageView ivPicJoke;
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
	        return mCache.get(url);  
	    	
	    }  
	  
	    @Override  
	    public void putBitmap(String url, Bitmap bitmap) {  
	        mCache.put(url, bitmap);  
	    }  
	  
	}  
}
