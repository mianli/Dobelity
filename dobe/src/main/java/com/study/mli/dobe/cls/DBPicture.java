package com.study.mli.dobe.cls;

/**
 * Created by crown on 2016/9/8.
 */
public class DBPicture {

	public String pictureId;
	public String imgUrl;
	public String thumbnail;
	public String title;
	public String hotLevel;

	public DBPicture(String pictrueId, String title, String imgUrl, String thumbnail, String hotLevel) {
		this.pictureId = pictrueId;
		this.imgUrl = imgUrl;
		this.thumbnail = thumbnail;
		this.title = title;
		this.hotLevel = hotLevel;
	}

	@Override
	public String toString() {
		return "data:" + thumbnail + "\n";
	}
}
