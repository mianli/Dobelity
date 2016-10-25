package com.study.mli.dobe.utils.loader;

import com.study.mli.dobe.cls.Picture;

import java.util.List;

/**
 * Created by crown on 2016/9/8.
 */
public interface iGetDataListener {

	void onFinish(int page, List<Picture> pictures, boolean success);
}
