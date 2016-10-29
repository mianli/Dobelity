package com.study.mli.dobe.utils.loader;

import android.os.Handler;
import android.util.Log;

import com.study.mli.dobe.cls.eImageType;
import com.study.mli.dobe.customview.GifImageView;
import com.study.mli.dobe.tools.DBLog;
import com.study.mli.dobe.utils.loader.cache.CacheHelper;
import com.study.mli.dobe.utils.loader.cache.DiskHelper;
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

    public ImageLoader() {
        mHandler = new Handler();
        executorService = Executors.newFixedThreadPool(20);
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

    private void filterTask(String url) {
        if(mLoadList.containsKey(url)) {
            WeakReference<Future> loader = mLoadList.get(url);
            Future future = loader.get();
            if(future != null) {
                if(future.cancel(true)) {
                    DBLog.i("-------------->cancel task");
                }
            }
        }
        mLoadList.remove(url);
    }

    private void loadImage(final String url,  final GifImageView imgv) {

        filterTask(url);
        byte[] data;
        if((data = CacheHelper.getInstance().getData(url) ) != null) {
            SetImageUtils.getInstance().setImageView(url, imgv, data);
        }else
        loadDiskImage(url, imgv);
    }

    private void loadImageByNet(final String url, final GifImageView imgv, final ILView ilView) {
        LoadImageHelper li = new LoadImageHelper(mHandler, url, imgv, new iLoadFinishListener() {
            @Override
            public void onFinish(boolean successful, final byte[] bytes) {
                if (successful && bytes != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CacheHelper.getInstance().saveData(url, bytes);

                            executorService.submit(new DiskHelper(url, bytes, mHandler, new iLoadFinishListener() {
                                @Override
                                public void onFinish(boolean successful, byte[] bytes) {
                                    //是否缓存成功
                                    if (!successful) {
                                        DBLog.i("write to disk failed");
                                    }
                                }
                            }));

                            SetImageUtils.getInstance().setImageView(url, imgv, bytes);
                        }
                    });
                }
            }
        });

        Future future = executorService.submit(li);
        mLoadList.put(url, new WeakReference<>(future));
    }

    private void loadDiskImage(final String url, final GifImageView gifImageView) {
        DiskHelper dh = new DiskHelper(url, mHandler, new iLoadFinishListener() {
            @Override
            public void onFinish(boolean successful, byte[] bytes) {
                if(successful) {
                    SetImageUtils.getInstance().setImageView(url, gifImageView, bytes);
                    CacheHelper.getInstance().saveData(url, bytes);
                }else {
                    loadImageByNet(url, gifImageView, null);
                }
            }
        });
        Future future = executorService.submit(dh);
        mLoadList.put(url, new WeakReference<>(future));
    }

}
