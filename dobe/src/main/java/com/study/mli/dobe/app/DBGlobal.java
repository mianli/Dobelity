package com.study.mli.dobe.app;


import android.os.Handler;

import com.study.mli.dobe.utils.loader.ParserUtils;
import com.study.mli.dobe.utils.cache.ILBytesCache;

/**
 * Created by crown on 2016/9/8.
 */
public class DBGlobal {

	public String URL = new String();
	public Handler mHandler;
	public ILBytesCache cache;

	public DBGlobal(Handler handler) {
		this.mHandler = handler;
		this.cache = new ILBytesCache();

	}

	public static DBGlobal mInstance;
	public static ParserUtils parser = new ParserUtils();

	public static void init(Handler handler) {
		if(mInstance == null) {
			mInstance = new DBGlobal(handler);
		}
	}

}