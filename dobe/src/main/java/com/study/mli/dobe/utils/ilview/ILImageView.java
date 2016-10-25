package com.study.mli.dobe.utils.ilview;

import com.study.mli.dobe.customview.GifImageView;

/**
 * Created by limian on 2016/9/18.
 */
public class ILImageView extends ILViewBase<GifImageView> {

    public ILImageView(GifImageView view) {
        super(view.hashCode() + "", view);
    }

}
