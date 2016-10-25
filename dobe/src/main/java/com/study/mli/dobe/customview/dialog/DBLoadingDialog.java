package com.study.mli.dobe.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.study.mli.dobe.R;

/**
 * Created by limian on 2016/9/13.
 */
public class DBLoadingDialog extends Dialog{

    private final long minDuration = 1500;
    private long time;
    public DBLoadingDialog(Context context) {
        super(context, R.style.Custom_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading_dialog);
    }

    @Override
    public void show() {
        super.show();
        time = System.currentTimeMillis();
    }

    @Override
    public void dismiss() {
        long t = System.currentTimeMillis();
        long duration = t - time;
        if(duration < minDuration) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, minDuration - duration);
        }else {
            super.dismiss();
        }
    }

}
