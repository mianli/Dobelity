package com.study.mli.dobe.utils.loader;

import com.study.mli.dobe.cls.DBPicture;

import java.util.List;

/**
 * Created by crown on 2016/9/8.
 */
public interface iGetDataListener {

	void onFinish(int page, List<DBPicture> pictures, boolean success);
}
