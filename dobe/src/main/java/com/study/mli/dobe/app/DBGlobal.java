package com.study.mli.dobe.app;


import android.os.Handler;

import com.study.mli.dobe.utils.loader.ParserUtils;

import java.io.File;

/**
 * Created by crown on 2016/9/8.
 */
public class DBGlobal {

	public String URL = new String();
	public Handler mHandler;
	public File ImageFile;
	public String imageFileName = "Dobe";

	public DBGlobal(Handler handler) {
		this.mHandler = handler;

	}

	public static DBGlobal mInstance;
	public static ParserUtils parser = new ParserUtils();

	public static void init(Handler handler) {
		if(mInstance == null) {
			mInstance = new DBGlobal(handler);
		}
	}

}
