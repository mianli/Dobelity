package com.study.mli.dobe.activity.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.study.mli.dobe.R;
import com.study.mli.dobe.activity.ShowImageActivity;
import com.study.mli.dobe.cls.Picture;
import com.study.mli.dobe.customview.GifImageView;
import com.study.mli.dobe.tools.DBLog;
import com.study.mli.dobe.utils.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crown on 2016/9/30.
 */
public class HomeAdapter extends BaseAdapter {

	private List<Picture> list = new ArrayList<>();
	private Activity mActivity;
	public HomeAdapter(Activity activity, List<Picture> list) {
		this.mActivity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MyViewHolder holder;
		if(convertView == null) {
			holder = new MyViewHolder();
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.layout_item, parent, false);
			holder.mImgv = (GifImageView) convertView.findViewById(R.id.gifview);
			holder.mTv = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		}else {
			holder = (MyViewHolder) convertView.getTag();
		}
		setOnItemTouchListener(position, holder, convertView);
		ImageLoader.getInstance().display(list.get(position).thumbnail, holder.mImgv);
		holder.mTv.setText(list.get(position).title);
		return convertView;
	}

	private void setOnItemTouchListener(final int position, final MyViewHolder viewHolder, final View view) {
		viewHolder.mImgv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setListener(position, viewHolder, view);
			}
		});
		viewHolder.mTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setListener(position, viewHolder, view);
			}
		});
	}

	private void setListener(int position, MyViewHolder viewHolder, View view) {
		float height = view.getHeight();
		float width = view.getWidth();
		float x = view.getX();
		float y = view.getY();

		ImageViewInfo info = new ImageViewInfo(x, y, width, height, list.get(position).thumbnail, list.get(position).imgUrl);
		Intent intent = new Intent(mActivity, ShowImageActivity.class);
		Bundle data = new Bundle();
		data.putSerializable(ShowImageActivity.IMAGEVIEW_INFO, info);
		intent.putExtras(data);
		mActivity.startActivity(intent);
		mActivity.overridePendingTransition(0, 0);
	}

	private class MyViewHolder{
		public TextView mTv;
		public GifImageView mImgv;
	}
}