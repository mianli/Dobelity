package com.study.mli.dobe.app;

import android.app.Application;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by crown on 2016/9/8.
 */
public class DApplication extends Application {


	@Override
	public void onCreate() {
		super.onCreate();
		Handler handler = new Handler();
		DBGlobal.init(handler);
		initUrl();

		DBGlobal.mInstance.ImageFile = this.getExternalFilesDir(DBGlobal.mInstance.imageFileName);
	}

	private void initUrl() {
		try {
			InputStream is = getResources().getAssets().open("properties.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			DBGlobal.mInstance.URL = new String(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
