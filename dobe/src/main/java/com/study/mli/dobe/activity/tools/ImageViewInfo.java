package com.study.mli.dobe.activity.tools;

import java.io.Serializable;

/**
 * Created by crown on 2016/9/30.
 */
public class ImageViewInfo implements Serializable {

	public float mOriginX;
	public float mOriginY;
	public float mOriginWidth;
	public float mOriginHeight;

	public String mThumbnail;
	public String mUrl;
	public byte[] mImageData;

	public ImageViewInfo(float x, float y, float width , float height, String thumbnail, String url) {
		this.mOriginX = x;
		this.mOriginY = y;
		this.mOriginWidth = width;
		this.mOriginHeight = height;
		this.mThumbnail = thumbnail;
		this.mUrl = url;
	}

}
