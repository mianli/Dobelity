package com.study.mli.dobe.utils.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.Environment;
import android.util.Log;

import com.study.mli.dobe.tools.DBLog;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by limian on 2016/10/25.
 */

public class DiskHelper {

	private static DiskHelper mInstance;

	public static DiskHelper getInstance() {
		if(mInstance == null) {
			mInstance = new DiskHelper();
		}
		return mInstance;
	}
	private String mParentPath;

	private String getFileName(String url) {
		return mParentPath + MD5Encoder.encode(url) + ".jpg";
	}

	public void save(String url, Bitmap bitmap) {
		createParentFileIfNeed();
		File file = new File(getFileName(url));
		if(!file.exists()) {
			createFile(file, bitmap);
		}
	}

	public void save2File(String url, byte[] bytes) {
		createParentFileIfNeed();
		File file = new File(getFileName(url));
		if(!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(bytes);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public byte[] readFromFile(String url) {
		createParentFileIfNeed();
		File file = new File(getFileName(url));
		FileInputStream fis = null;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		if(file.exists()) {
			try {
				fis = new FileInputStream(file);
				byte[] buffer = new byte[fis.available()];
				int length;
				while ((length = fis.read()) != -1) {
					bout.write(buffer, 0, length);
				}
				return buffer;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}finally {
				try {
					fis.close();
					bout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public Bitmap get(String url) {
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			createParentFileIfNeed();
			File file = new File(getFileName(url));
			if(file.exists()){
				Bitmap bitmap = BitmapFactory.decodeFile(getFileName(url));
				return bitmap;
			}
		}
		return null;
	}

	private void createParentFileIfNeed() {
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			mParentPath = sdcardDir.getPath() + "/Dobelity/";
			File path = new File(mParentPath);
			if(!path.exists()) {
				boolean createResult = path.mkdirs();
				DBLog.i("result :" + createResult);
			}
		}
	}

	private void createFile(File file, Bitmap bitmap) {
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private String getSDCardPath() {
		File SDDir = null;
		boolean exist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if(exist) {
			SDDir = Environment.getExternalStorageDirectory();
		}
		if(SDDir != null) {
			return SDDir.toString();
		}
		return null;
	}
}
