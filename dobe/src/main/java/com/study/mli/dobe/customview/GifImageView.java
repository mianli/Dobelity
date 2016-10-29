package com.study.mli.dobe.customview;

/**
 * Created by crown on 2016/9/12.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.study.mli.dobe.R;

import java.lang.ref.WeakReference;

/**
 * Created by Cuneyt on 4.10.2015.
 * Gifview
 */
public class GifImageView extends ImageView {

	private static final int DEFAULT_MOVIE_VIEW_DURATION = 1000;

	private int mMovieResourceId;
//	private Movie movie;
	private Movie movie;

	private long mMovieStart;
	private int mCurrentAnimationTime;

	private String mUrl;

	/**
	 * Position for drawing animation frames in the center of the view.
	 */
	private float mLeft;
	private float mTop;

	/**
	 * Scaling factor to fit the animation within view bounds.
	 */
	private float mScale;

	/**
	 * Scaled movie frames width and height.
	 */
	private int mMeasuredMovieWidth;
	private int mMeasuredMovieHeight;

	private volatile boolean mPaused;
	private boolean mVisible = true;
	private byte[] bytes;

	public GifImageView(Context context) {
		this(context, null);
	}

	public GifImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GifImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setViewAttributes(context, attrs, defStyle);
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	@SuppressLint("NewApi")
	private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {

		/**
		 * Starting from HONEYCOMB(Api Level:11) have to turn off HW acceleration to draw
		 * Movie on Canvas.
		 */
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		final TypedArray array = context.obtainStyledAttributes(attrs,
			R.styleable.GifView, defStyle, R.style.Widget_GifView);

		//-1 is default value
		mMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1);
		mPaused = array.getBoolean(R.styleable.GifView_paused, false);

		array.recycle();

		if (mMovieResourceId != -1) {
			movie =Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
		}
	}

	public void release() {
		Drawable drawable = getDrawable();
		if(drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			if(bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
	}

	public boolean setBytes(byte[] bytes) {
//		if(bytes != null) {
//			bytes = null;
//		}
		this.bytes = bytes;
		if(bytes != null) {
			Movie movie = getMovie(bytes);
			if(movie != null) {
				setMovie(movie);
				return true;
			}else {
				setImageview(bytes);
				return false;
			}
		}
		return false;
	}

	//if showing gif or not when showing
	public boolean showGif() {
		if(movie != null) {
			return true;
		}
		return false;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public Movie getMovie(byte[] bytes) {
		return Movie.decodeByteArray(bytes, 0, bytes.length);
	}

	public void setMovie(Movie movie) {
		release();
		if(movie == null || !movie.equals(this.movie)) {
			this.movie= movie;
			requestLayout();
		}
	}

	public void setImageview(byte[] bytes) {
		release();
		Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		if(bm != null) {
			setImageBitmap(bm);
		}
	}

	public Movie getMovie() {
		return this.movie;
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if(movie != null) {
			setMovie(null);
		}
		super.setImageBitmap(bm);
	}

	public void setGifResource(int movieResourceId) {
		this.mMovieResourceId = movieResourceId;
		movie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
		requestLayout();
	}

	public int getGifResource() {

		return this.mMovieResourceId;
	}


	public void play() {
		if (this.mPaused) {
			this.mPaused = false;

			/**
			 * Calculate new movie start time, so that it resumes from the same
			 * frame.
			 */
			mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;

			invalidate();
		}
	}

	public void pause() {
		if (!this.mPaused) {
			this.mPaused = true;

			invalidate();
		}

	}


	public boolean isPaused() {
		return this.mPaused;
	}

	public boolean isPlaying() {
		return !this.mPaused;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (movie != null) {
			int movieWidth = movie.width();
			int movieHeight = movie.height();

			/*
             * Calculate horizontal scaling
			 */
			float scaleH = 1f;
			int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);

			if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
				int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
				if (movieWidth > maximumWidth) {
					scaleH = (float) movieWidth / (float) maximumWidth;
				}
			}

			/*
             * calculate vertical scaling
			 */
			float scaleW = 1f;
			int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);

			if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
				int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
				if (movieHeight > maximumHeight) {
					scaleW = (float) movieHeight / (float) maximumHeight;
				}
			}

			/*
             * calculate overall scale
			 */
			mScale = 1f / Math.max(scaleH, scaleW);

			mMeasuredMovieWidth = (int) (movieWidth * mScale);
			mMeasuredMovieHeight = (int) (movieHeight * mScale);

			setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);

		} else {
			/*
			 * No movie set, just set minimum available size.
			 */
//			setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(movie != null) {
		/*
		 * Calculate mLeft / mTop for drawing in center
		 */
			mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
			mTop = (getHeight() - mMeasuredMovieHeight) / 2f;

			mVisible = getVisibility() == View.VISIBLE;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (movie != null) {
			if (!mPaused) {
				updateAnimationTime();
				drawMovieFrame(canvas);
				invalidateView();
			} else {
				drawMovieFrame(canvas);
			}
		}else {
			super.onDraw(canvas);
		}
	}

	/**
	 * Invalidates view only if it is mVisible.
	 * <br>
	 * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
	 */
	@SuppressLint("NewApi")
	private void invalidateView() {
		if (mVisible) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				postInvalidateOnAnimation();
			} else {
				invalidate();
			}
		}
	}

	/**
	 * Calculate current animation time
	 */
	private void updateAnimationTime() {
		long now = android.os.SystemClock.uptimeMillis();

		if (mMovieStart == 0) {
			mMovieStart = now;
		}

		int dur = movie.duration();

		if (dur == 0) {
			dur = DEFAULT_MOVIE_VIEW_DURATION;
		}

		mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
	}

	/**
	 * Draw current GIF frame
	 */
	private void drawMovieFrame(Canvas canvas) {
		movie.setTime(mCurrentAnimationTime);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(mScale, mScale);
		movie.draw(canvas, mLeft / mScale, mTop / mScale);
		canvas.restore();
	}

	@SuppressLint("NewApi")
	@Override
	public void onScreenStateChanged(int screenState) {
		super.onScreenStateChanged(screenState);
		if(movie != null) {
			mVisible = screenState == SCREEN_STATE_ON;
			invalidateView();
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if(movie != null) {
			mVisible = visibility == View.VISIBLE;
			invalidateView();
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if(movie != null) {
			mVisible = visibility == View.VISIBLE;
			invalidateView();
		}
	}

	private boolean canShowInternalGif() {
		return mUrl != null && mUrl.equals(getTag());
	}

}