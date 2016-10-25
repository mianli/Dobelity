package com.study.mli.dobe.tools;

import android.util.Log;

/**
 * Created by crown on 2016/9/29.
 */
public class DBLog {

	private static final String TAG = "crown";

	public static void i(String msg) {
		Log.i(TAG, msg);
	}

	public static void i(int intMsg) {
		Log.i(TAG, "" + intMsg);
	}

}
