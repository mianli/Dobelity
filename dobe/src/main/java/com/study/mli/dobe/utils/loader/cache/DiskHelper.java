package com.study.mli.dobe.utils.loader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;

import com.study.mli.dobe.app.DBGlobal;
import com.study.mli.dobe.cls.eImageType;
import com.study.mli.dobe.tools.DBLog;
import com.study.mli.dobe.utils.loader.SetImageUtils;
import com.study.mli.dobe.utils.loader.iLoadFinishListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by limian on 2016/10/25.
 */

public class DiskHelper implements Runnable{

	public enum eType {
		eWrite,
		eRead
	}
	private eType mType = eType.eRead;
	private iLoadFinishListener mListener;
	private Handler mHandler;
	private String mUrl;
	private byte[] mData;

	private String mParentPath;

	public DiskHelper(String url, Handler handler, iLoadFinishListener listener) {
		this.mHandler = handler;
		this.mUrl = url;
		this.mListener = listener;
		this.mType = eType.eRead;
	}

	public DiskHelper(String url, WeakReference<byte[]> bytes, Handler handler, iLoadFinishListener listener) {
		this.mHandler = handler;
		this.mUrl = url;
		this.mData = bytes.get();
		this.mListener = listener;
		this.mType = eType.eWrite;
	}

	@Override
	public void run() {
		if(mType == eType.eRead) {
			final byte[] data = readFromFile(mUrl);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mListener.onFinish(data != null, new WeakReference<byte[]>(data));
				}
			});
		}else if(mType == mType.eWrite) {
			save2File(mUrl, mData, SetImageUtils.mInstance.isGifImage(mData) ? eImageType.eGif : eImageType.eImage);
		}
	}

	private String getFileName(String url, eImageType imageType) {
		String path = mParentPath + MD5Encoder.encode(url);
		if(imageType == eImageType.eGif) {
			return path + ".gif";
		}else {
			return path + ".jpg";
		}
	}

	public void save2File(String url, byte[] bytes, eImageType imageType) {
		createParentFileIfNeed();
		File file = new File(getFileName(url, imageType));
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
		File file = new File(getFileName(url, eImageType.eImage));
		FileInputStream fis = null;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		if(file.exists()) {
			try {
				fis = new FileInputStream(file);
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
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
			File file = new File(getFileName(url, eImageType.eImage));
			if(file.exists()){
				Bitmap bitmap = BitmapFactory.decodeFile(getFileName(url, eImageType.eImage));
				return bitmap;
			}
		}
		return null;
	}

	private void createParentFileIfNeed() {
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//			File sdcardDir = Environment.getExternalStorageDirectory();
//			mParentPath = DBGlobal.mInstance.ImageFile + "/Dobelity/";
			File path = DBGlobal.mInstance.ImageFile;
			mParentPath = path.getPath() + "/";
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

}
