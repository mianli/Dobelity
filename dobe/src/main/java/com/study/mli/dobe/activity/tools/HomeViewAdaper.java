package com.study.mli.dobe.activity.tools;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.mli.dobe.R;
import com.study.mli.dobe.cls.DBPicture;
import com.study.mli.dobe.customview.GifImageView;
import com.study.mli.dobe.utils.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crown on 2016/10/1.
 */
@Deprecated
public class HomeViewAdaper extends RecyclerView.Adapter<HomeViewAdaper.HomeViewHolder> {

	private List<DBPicture> list = new ArrayList<>();
	private Activity mActivity;

	public HomeViewAdaper(Activity activity, List<DBPicture> list) {
		this.mActivity = activity;
		this.list = list;
	}

	@Override
	public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		HomeViewHolder holder =
			new HomeViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.layout_item, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(HomeViewHolder holder, int position) {
		ImageLoader.getInstance().display(list.get(position).thumbnail, holder.mImgv);
		holder.mTv.setText(list.get(position).title);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public class HomeViewHolder extends RecyclerView.ViewHolder {

		public TextView mTv;
		public GifImageView mImgv;

		public HomeViewHolder(View itemView) {
			super(itemView);

			mImgv = (GifImageView) itemView.findViewById(R.id.gifview);
			mTv = (TextView) itemView.findViewById(R.id.tv);
		}
	}

}
