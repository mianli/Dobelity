package com.study.mli.dobe.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by limian on 2016/9/12.
 */
@Deprecated
public class GifImage extends ImageView {

    private Movie mMovie;
    private long movieStart;

    public GifImage(Context context) {
        super(context);
    }

    public GifImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GifImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long curTime=android.os.SystemClock.uptimeMillis();
        if(movieStart==0){
            movieStart=curTime;
        }
        if(mMovie!=null){
            int duration=mMovie.duration();
            int relTime=(int)((curTime-movieStart)%duration);
            mMovie.setTime(relTime);
            mMovie.draw(canvas,0,0);
            invalidate();
        }
        super.onDraw(canvas);
    }

}
