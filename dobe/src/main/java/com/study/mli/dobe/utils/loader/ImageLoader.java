package com.study.mli.dobe.utils.loader;

import android.os.Handler;
import android.util.Log;

import com.study.mli.dobe.app.DBGlobal;
import com.study.mli.dobe.customview.GifImageView;
import com.study.mli.dobe.tools.DBLog;
import com.study.mli.dobe.utils.cache.CacheHelper;
import com.study.mli.dobe.utils.cache.DiskHelper;
import com.study.mli.dobe.utils.ilview.ILImageView;
import com.study.mli.dobe.utils.ilview.ILView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by limian on 2016/9/9.
 */
public class ImageLoader{

    private ExecutorService executorService;
    private Map<String, WeakReference<Future>> mLoadList = new HashMap<>();
    private Handler mHandler;
    public static ImageLoader mInstance;

    private int times = 0;

    private Map<String, ILView> mViews = new HashMap<>();

    public ImageLoader() {
        mHandler = new Handler();
        executorService = Executors.newCachedThreadPool();
    }

    public static ImageLoader getInstance() {
        if(mInstance == null) {
            mInstance = new ImageLoader();
        }
        return mInstance;
    }

    public void display(final String url, final GifImageView imgv) {
        imgv.setTag(url);
        imgv.setUrl(url);

        loadImage(url, imgv);
    }

    private void startLoadView(ILView view) {
        for (ILView v : mViews.values()) {
            if(view.get().equals(v.get())) {
                v.clear();
            }
        }
        mViews.put(view.hashCode() + "", view);
    }

    private void loadImage(final String url,  final GifImageView imgv) {
        final ILView ilView = new ILImageView(imgv);

        startLoadView(ilView);

        if(mLoadList.containsKey(url)) {
            WeakReference<Future> loader = mLoadList.get(url);
            Future future = loader.get();
            if(future != null) {
                if(future.cancel(true)) {
                    Log.i("testtesttest", "取消了一个线程");
                }
            }
        }

        byte[] data;
//        if((data = CacheHelper.getInstance().getData(url) ) != null) {
//            SetImageUtils.getInstance().setImageview(imgv, data);
//            DBLog.i("load form cache");
//        }else
        if((data = DiskHelper.getInstance().readFromFile(url)) != null) {
            SetImageUtils.getInstance().setImageview(imgv, data);
            CacheHelper.getInstance().saveData(url, data);
            DBLog.i("load form sd card");
        }else {
            loadImageByNet(url, imgv, ilView);
        }
    }

    private void loadImageByNet(final String url, final GifImageView imgv, final ILView ilView) {
        LoadImage li = creator(mHandler, url, imgv, new iLoadFinishListener() {
            @Override
            public void onFinish(boolean successful, final byte[] bytes) {
                if(successful && bytes != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(DBGlobal.mInstance.cache.getCache(url) == null) {
                                DBGlobal.mInstance.cache.saveInCache(url, bytes);
                            }

                            if (mViews.get(ilView.hashCode() + "") == null || mViews.get(ilView.hashCode() + "").invalidate()) {
                                return;
                            }

                            CacheHelper.getInstance().saveData(url, bytes);
                            DiskHelper.getInstance().save2File(url, bytes);
                            SetImageUtils.getInstance().setImageView(url, imgv, bytes);
                            Log.i("testtesttest", "" + times++);
                        }
                    });
                }
            }
        });
        Future future = executorService.submit(li);//.execute(li);
        mLoadList.put(url, new WeakReference<>(future));
    }

    LoadImage creator(Handler handler, String url, GifImageView imgv, iLoadFinishListener listener) {
        return new LoadImage(handler, url, imgv, listener);
    }

}
