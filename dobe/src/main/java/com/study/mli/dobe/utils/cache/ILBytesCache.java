package com.study.mli.dobe.utils.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by crown on 2016/9/29.
 */
public class ILBytesCache {

	// FIXME: 2016/9/29 这里的缓存当数量大于100的时候会全部清空，有时间处理一下
	private static final int MAX_CACHE = 100;

	public Map<String, byte[]> bytesCache = new HashMap<>();

	public void saveInCache(String url, byte[] bytes) {
		if(bytesCache.size() > MAX_CACHE) {
			bytesCache.clear();
		}
		byte[] b = bytesCache.get(url);
		if(b != null) {
			bytesCache.remove(url);
		}
		bytesCache.put(url, bytes);
	}

	public byte[] getCache(String url) {
		return bytesCache.get(url);
	}

}
