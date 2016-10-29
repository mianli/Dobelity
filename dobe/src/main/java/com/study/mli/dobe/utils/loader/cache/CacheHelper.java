package com.study.mli.dobe.utils.loader.cache;

import android.util.LruCache;

import com.study.mli.dobe.tools.DBLog;

import java.lang.ref.WeakReference;

/**
 * Created by limian on 2016/10/25.
 */

public class CacheHelper {

	private LruCache<String, byte[]> mLruCache;

	public static CacheHelper mInstance = null;

	public static CacheHelper getInstance() {
		if(mInstance == null) {
			mInstance = new CacheHelper();
		}
		return mInstance;
	}

	public CacheHelper() {
		int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
		mLruCache = new LruCache<String, byte[]>(maxSize) {
			@Override
			protected int sizeOf(String key, byte[] value) {
				return value.length;
			}
		};
	}

	public void saveData(String url, WeakReference<byte[]> bytes) {
		if(url != null && bytes != null && bytes.get() != null) {
			mLruCache.put(url, bytes.get());
		}else {
			DBLog.i("something null");
		}
	}

	public byte[] getData(String url) {
		return mLruCache.get(url);
	}

}
