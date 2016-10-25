package com.study.mli.dobe.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by crown on 2016/9/30.
 */
public class DBDeviceUtils {

	private static int mScreenWidth;
	private static int mScreenHeight;

	public static int getWidth(Activity activity) {
		if(mScreenWidth == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			mScreenWidth = dm.widthPixels;
		}
		return mScreenWidth;
	}

	public static int getScreenHeight(Activity activity) {
		if(mScreenHeight == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			mScreenHeight = dm.heightPixels;
		}
		return mScreenHeight;
	}

	public static int getHeight(Activity activity) {
		if(mScreenHeight == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			mScreenHeight = dm.heightPixels;
		}
		return mScreenHeight - getActionBarHeight(activity);
	}

	public static int getActionBarHeight(Activity activity) {
		return activity.getActionBar().getHeight();
	}

}
