package com.study.mli.dobe.utils.ilview;

/**
 * Created by crown on 2016/9/29.
 */
public interface ILView {

	String getKey();
	Object get();
	boolean invalidate();
	void clear();

}
