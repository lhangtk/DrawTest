package com.example.drawtest;

import android.graphics.Paint;
import android.graphics.Path;

public class PathInfo {
	/** 路径 */
	private Path mPath;
	/** 画笔 */
	private Paint mPaint;
	/** 画笔时间 */
	private long time;

	public PathInfo(Path mPath, Paint mPaint, long time) {
		super();
		this.mPath = mPath;
		this.mPaint = mPaint;
		this.time = time;
	}

	/**
	 * @return the mPath
	 */
	public Path getmPath() {
		return mPath;
	}

	/**
	 * @param mPath
	 *            the mPath to set
	 */
	public void setmPath(Path mPath) {
		this.mPath = mPath;
	}

	/**
	 * @return the mPaint
	 */
	public Paint getmPaint() {
		return mPaint;
	}

	/**
	 * @param mPaint
	 *            the mPaint to set
	 */
	public void setmPaint(Paint mPaint) {
		this.mPaint = mPaint;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}
}
