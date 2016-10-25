package com.study.mli.dobe.utils.loader;

import android.os.Handler;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.study.mli.dobe.app.DBGlobal;
import com.study.mli.dobe.customview.GifImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by crown on 2016/9/19.
 */
class LoadImage implements Runnable {

	GifImageView mImgV;
	String url;
	Handler handler;
	private iLoadFinishListener mFinishListener;

	public LoadImage(Handler handler, String url, GifImageView imgv, iLoadFinishListener listener) {
		this.handler = handler;
		this.url = url;
		this.mImgV = imgv;
		this.mFinishListener = listener;
		byte[] bytes = DBGlobal.mInstance.cache.getCache(url);
		if(bytes != null) {
			SetImageUtils.getInstance().setImageView(url, imgv, bytes);
		}else {
			this.mImgV.setImageResource(SetImageUtils.DEFAULT_IMAGE_RESOURCE);
		}
	}

	@Override
	public void run() {
		byte[] cache = DBGlobal.mInstance.cache.getCache(url);
		if(cache != null) {
			mFinishListener.onFinish(true, cache);
		}else {
			loadUrl();
		}
	}

	private void loadUrl() {
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				mFinishListener.onFinish(false, null);
			}

			@Override
			public void onResponse(Response response) throws IOException {
				final InputStream is = response.body().byteStream();
				final byte[] bytes = InputStreamToByte(is);
				mFinishListener.onFinish(true, bytes);
			}
		});

	}

	public byte[] InputStreamToByte(InputStream is){
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		try {
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private InputStream getInputstream() {
		InputStream is = null;
		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			connection.disconnect();
			return is;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}


