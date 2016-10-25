package com.study.mli.dobe.utils.loader;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by crown on 2016/9/12.
 */
public class ImageCacheUtils {

	public static ImageCacheUtils mInstance;

	// TODO: 2016/9/12 修改这个地方
	private final String FILE_NAME = "dobe";
	private Context mContext;

	public ImageCacheUtils getInstance(Context context) {
		if(mInstance == null) {
			mInstance = new ImageCacheUtils();
		}
		mContext = context;
		return mInstance;
	}

	public ImageCacheUtils() {

	}

	public ImageCacheUtils(Context context) {
		this.mContext = context;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	private File getCacheFile(String fileName) {
//		File file = new File(getContext().getCacheDir(), FILE_NAME);
		return new File(getContext().getCacheDir(), fileName);
	}

	public void write2File(String key, byte[] data) {

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(getCacheFile(key));
			outputStream.write(data);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public byte[] readFromFile(String filename) {
		FileInputStream inputStream;
		ByteArrayOutputStream bout;
		byte[]buf = new byte[1024];
		bout = new ByteArrayOutputStream();
		int length;
		try {
			inputStream = new FileInputStream(getCacheFile(filename));
			while((length = inputStream.read(buf))!=-1){
				bout.write(buf,0,length);
			}
			byte[] content = bout.toByteArray();
			inputStream.close();
			bout.close();
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public boolean isExist(String filename, byte[] data) {
		byte[] bytes = readFromFile(filename);
		if(data.length != bytes.length) {
			return false;
		}
		for (int i = 0; i < data.length; i++) {
		}
		return false;
	}

	public Context getContext() {
		return mContext;//.getApplicationContext();
	}

}
