package com.example.drawtest;

import android.graphics.Paint;

public class PointInfo {
	/** x坐标 */
	private float x;
	/** y坐标 */
	private float y;
	/** 记录时间 */
	private long time;
	/** 是否为结束点 */
	private boolean end;
	/** 是否为开始点 */
	private boolean begin;
	/** 画笔 */
	private Paint mPaint;

	public PointInfo(float x, float y, long time, Paint mPaint, boolean begin,
			boolean end) {
		super();
		this.x = x;
		this.y = y;
		this.time = time;
		this.mPaint = mPaint;
		this.begin = begin;
		this.end = end;
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
	 * @return the begin
	 */
	public boolean isBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(boolean begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public boolean isEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(boolean end) {
		this.end = end;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		this.y = y;
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
