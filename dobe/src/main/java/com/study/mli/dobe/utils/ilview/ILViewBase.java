package com.study.mli.dobe.utils.ilview;

import java.lang.ref.WeakReference;

/**
 * Created by limian on 2016/9/18.
 */
public class ILViewBase<VIEW_TYPE> implements ILView{
    public WeakReference<VIEW_TYPE> mView;
    public String mKey;

    public void setmView(WeakReference<VIEW_TYPE> mView) {
        this.mView = mView;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public WeakReference<VIEW_TYPE> getmView() {
        return mView;
    }


    public VIEW_TYPE getView() {
        return mView.get();
    }

    public ILViewBase(String url, VIEW_TYPE view) {
        this.mKey = url;
        this.mView = new WeakReference<VIEW_TYPE>(view);
    }

    public ILViewBase(VIEW_TYPE view) {
        this.mView = new WeakReference<VIEW_TYPE>(view);
        if(view != null) {
            this.mKey = mView.hashCode() + "";
        }
    }

    public boolean valid() {
        return getmView() != null;
    }

    public void inValid() {
        getmView().clear();
    }

    @Override
    public String getKey() {
        return mKey;
    }

    @Override
    public VIEW_TYPE get() {
        if(mView == null) {
            return null;
        }else if(mView.get() != null) {
            return mView.get();
        }
        return null;
    }

    @Override
    public boolean invalidate() {
        return mView == null || mView.get() == null;
    }

    @Override
    public void clear() {
        if(mView != null) {
            mView.clear();
        }
    }
}
