package com.study.mli.dobe.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.study.mli.dobe.R;
import com.study.mli.dobe.activity.tools.ImageViewInfo;
import com.study.mli.dobe.app.DBGlobal;
import com.study.mli.dobe.customview.GifImageView;
import com.study.mli.dobe.tools.DBLog;
import com.study.mli.dobe.utils.DBDeviceUtils;
import com.study.mli.dobe.utils.loader.ImageLoader;
import com.study.mli.dobe.utils.loader.cache.CacheHelper;
import com.study.mli.dobe.utils.loader.cache.DiskHelper;

/**
 * Created by crown on 2016/9/30.
 */
public class ShowImageActivity extends Activity {

	public static final String IMAGEVIEW_INFO = "imageViewInfo";
	private static final int ANIMATION_DURATION = 500;

	private ImageViewInfo mInfo;
	private GifImageView mImgv;
	private View mCover;
	private LinearLayout mContainer;


	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image);

		mInfo = (ImageViewInfo) getIntent().getSerializableExtra(IMAGEVIEW_INFO);
		if(mInfo != null) {
			getCenter();
			initView();
			mImgv.post(new Runnable() {
				@Override
				public void run() {
					startAnimation();
				}
			});

		}

	}

	private void initView() {
		mCover = findViewById(R.id.si_cover);
		mContainer = (LinearLayout) findViewById(R.id.si_imgv_container);
		mImgv = (GifImageView) findViewById(R.id.si_imgv);

		byte[] data;
		if((data = CacheHelper.getInstance().getData(mInfo.mThumbnail)) != null) {
			mImgv.setBytes(data);
		}
	}

	private int[] getCenter() {
		int centerX = DBDeviceUtils.getWidth(this) / 2;
		int centerY = DBDeviceUtils.getHeight(this) / 2;
		return new int[]{centerX, centerY};
	}

	private void startAnimation() {

		int left = (int) mInfo.mOriginX;
		int top = (int) mInfo.mOriginY;
		int right = (int) (left + mInfo.mOriginWidth);
		int bottom = (int) (top + mInfo.mOriginHeight);

		Rect oldRect = new Rect(left, top, right, bottom);
		Rect newRect = new Rect(0, 0, mCover.getWidth(), mCover.getHeight());

		float offsetX = Math.abs(oldRect.left - newRect.left);
		float offsetY = Math.abs(oldRect.top - newRect.top);
		double offsetPosition = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
		long time = (long) (offsetPosition / 100);

		ValueAnimator containerAnimator = ValueAnimator.ofObject(new FrameEvaluator(), oldRect, newRect);

		containerAnimator.setDuration(ANIMATION_DURATION);
		containerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Rect rect = (Rect) animation.getAnimatedValue();
				mContainer.setX(rect.left);
				mContainer.setY(rect.top);

				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mContainer.getLayoutParams();
				lp.width = rect.right - rect.left;
				lp.height = rect.bottom - rect.top;
				mContainer.setLayoutParams(lp);
			}
		});

		containerAnimator.setDuration(ANIMATION_DURATION);
		ObjectAnimator animator = ObjectAnimator.ofFloat(mCover, "alpha", 0, 1);
		animator.setDuration(ANIMATION_DURATION);

		AnimatorSet set = new AnimatorSet();
		set.play(animator).with(containerAnimator);
		set.start();

		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				ImageLoader.getInstance().display(mInfo.mUrl, mImgv);
			}
		});
	}

	private class FrameEvaluator implements TypeEvaluator<Rect> {

		@Override
		public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
			int left = (int) ((startValue.left - endValue.left) * (1 - fraction));
			int top = (int) ((startValue.top - endValue.top) * (1- fraction));
			int right = (int) (endValue.right - (endValue.right - startValue.right) * (1 - fraction));
			int bottom = (int) (endValue.bottom - (endValue.bottom - startValue.bottom) * (1 - fraction));
			return new Rect(left, top, right, bottom);
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, 0);
	}

}
