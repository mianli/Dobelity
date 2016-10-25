package com.study.mli.dobe.customview.loader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by crown on 2016/10/1.
 */
public class PullableRecyclerView extends RecyclerView implements Pullable{
	public PullableRecyclerView(Context context) {
		super(context);
	}

	public PullableRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if(isSlideToBottom(this)) {
			return true;
		}
		return false;
	}

	public boolean isSlideToBottom(RecyclerView recyclerView) {
		if (recyclerView == null) return false;
		if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
			>= recyclerView.computeVerticalScrollRange())
			return true;
		return false;
	}

}
