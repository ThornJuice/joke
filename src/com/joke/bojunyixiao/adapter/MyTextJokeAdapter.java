package com.joke.bojunyixiao.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joke.bojunyixiao.R;
import com.joke.bojunyixiao.entity.TextJoke;


public class MyTextJokeAdapter extends BaseAdapter {
	protected static final String tag = "MyTextJokeAdapter";
	private Context context;
	private  List<TextJoke> jokes;

	public MyTextJokeAdapter(Context context, List<TextJoke> jokes) {
		this.context = context;
		this.jokes = jokes;
	}

	@Override
	public int getCount() {
		return jokes.size();
	}

	@Override
	public Object getItem(int arg0) {
		return jokes.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_textjoke, null);
			holder = new ViewHolder();
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.tvText = (TextView) convertView.findViewById(R.id.tvText);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvDate.setText(jokes.get(position).getUpDatetime());
		
		holder.tvText.setText(jokes.get(position).getText());
		return convertView;
	}

	static class ViewHolder {
		TextView tvDate;
		TextView tvText;
	}
}
