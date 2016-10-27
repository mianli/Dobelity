package com.study.mli.dobe.utils.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;

import com.study.mli.dobe.R;
import com.study.mli.dobe.customview.GifImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by crown on 2016/9/20.
 */
public class SetImageUtils {

	public static int DEFAULT_IMAGE_RESOURCE = R.drawable.default_img;

	public static SetImageUtils mInstance;

	public static SetImageUtils getInstance() {
		if(mInstance == null) {
			mInstance = new SetImageUtils();
		}
		return mInstance;
	}

	public void setImageView(String url, GifImageView gifview, byte[] bytes) {
		if(url != null && url.equals(gifview.getTag())) {
			gifview.setBytes(bytes);
		}
	}

	public void setImageView(GifImageView gifview, byte[] bytes) {
		Movie movie = getMovie(bytes);
		if(movie != null) {
			gifview.setMovie(movie);
		}else {
			setImageview(gifview, bytes);
		}
	}

	public void setImageview(GifImageView gifView, byte[] bytes) {
		Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		if(bm == null) {
			gifView.setImageResource(DEFAULT_IMAGE_RESOURCE);
		}else {
			gifView.setImageBitmap(bm);
		}
	}

	public boolean isGifImage(byte[] bytes) {
		if(getMovie(bytes) != null) {
			return true;
		}else {
			return false;
		}
	}

	public Movie getMovie(byte[] bytes) {
		return Movie.decodeByteArray(bytes, 0, bytes.length);
	}

	public void setGifImageView(GifImageView gifview, byte[] bytes) {
		gifview.setMovie(getMovie(bytes));
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

}