package com.study.mli.dobe.utils.loader;

import com.study.mli.dobe.customview.GifImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by crown on 2016/9/24.
 */
public class Loader {

	public static Loader instance;

	public static Loader getInstance() {
		if(instance == null) {
			instance = new Loader();
		}
		return instance;
	}

	Map<String, WeakReference<ImageLoader>> map = new HashMap<>();

	public void display(String url, GifImageView gifView) {
		if(map.get(url) != null && map.get(url).get() != null && !url.equals(gifView.getTag())) {
			map.get(url).clear();
		}
		gifView.setTag(url);
		ImageLoader imageLoader = new ImageLoader();
		map.put(url, new WeakReference<ImageLoader>(imageLoader));
		imageLoader.display(url, gifView);
	}

}
