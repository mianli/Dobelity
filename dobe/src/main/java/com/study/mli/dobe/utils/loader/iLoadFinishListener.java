package com.study.mli.dobe.utils.loader;

import java.lang.ref.WeakReference;

/**
 * Created by crown on 2016/9/24.
 */
public interface iLoadFinishListener {

	void onFinish(boolean successful, WeakReference<byte[]> bytes);

}
