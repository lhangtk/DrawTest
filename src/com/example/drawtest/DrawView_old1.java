package com.example.drawtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView_old1 extends View {
	float preX;
	float preY;
	private Path path;
	public Paint paint = null;
	// 定义一个内存中的图片，该图片将作为缓冲区
	private Bitmap cacheBitmap = null;
	// 定义cacheBitmap上的Canvas对象
	private Canvas cacheCanvas = null;
	/** 橡皮擦图标 */
	private Bitmap clear_bitmap;
	/** 当前状态是否为橡皮擦 */
	private boolean flag_earser = false;
	/** 判断是否在触摸屏幕，当手指抬起不绘制橡皮擦图标 */
	private boolean flag_up = true;
	/** 当前状态是否为激光笔状态 */
	private boolean flag_laser = false;
	private boolean flag_laser2 = false;

	private List<PathInfo> listPathInfos;

	private List<PointInfo> listPointInfos;

	public DrawView_old1(Context context, AttributeSet attr) {
		super(context, attr);
		// 初始化橡皮擦图标
		clear_bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bc_54f);
		// 初始化激光笔path列表
		listPathInfos = new ArrayList<PathInfo>();
		listPointInfos = new ArrayList<PointInfo>();
		// // 创建一个与该View相同大小的缓存区
		// cacheBitmap = Bitmap.createBitmap(this.getMeasuredWidth(),
		// this.getMeasuredHeight(), Config.ARGB_8888);
		// cacheCanvas = new Canvas();
		// path = new Path();
		// // 设置cacheCanvas将会绘制到内存中的cacheBitmap上
		// cacheCanvas.setBitmap(cacheBitmap);
		// // 设置画笔的颜色
		// paint = new Paint(Paint.DITHER_FLAG);
		// paint.setColor(Color.RED);
		// // 设置画笔风格
		// paint.setStyle(Paint.Style.STROKE);
		// paint.setStrokeWidth(5);
		// // 反锯齿
		// paint.setAntiAlias(true);
		// paint.setDither(true);
		//
		// paint.setStrokeJoin(Paint.Join.ROUND);
		// paint.setStrokeCap(Paint.Cap.ROUND);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 创建一个与该View相同大小的缓存区
		// 无法再构造函数中获取view的宽高
		cacheBitmap = Bitmap.createBitmap(
				MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec), Config.ARGB_8888);
		cacheCanvas = new Canvas();
		path = new Path();
		// 设置cacheCanvas将会绘制到内存中的cacheBitmap上
		cacheCanvas.setBitmap(cacheBitmap);
		// 设置画笔的颜色
		paint = new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.RED);
		// 设置画笔风格
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		// 反锯齿
		paint.setAntiAlias(true);
		paint.setDither(true);

		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);

	}

	/**
	 * 设置view的背景图片
	 * 
	 * @param bitmap
	 *            背景图片
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setBitmap(Bitmap bitmap) {
		// int w = bitmap.getWidth();// 图片宽
		// int h = bitmap.getHeight();// 图片高
		// // 调整图片
		// if (w > this.getWidth()) {
		// float r = (float) w / (float) this.getWidth();
		// w = this.getWidth();
		// h = (int) (h / r);
		// } else if (h > this.getHeight()) {
		// float r = (float) h / (float) this.getHeight();
		// h = this.getHeight();
		// w = (int) (w / r);
		// }
		// mCanvas .setBitmap(mBitmap)
		// 会先判断bitmap.isMutable()是否为true，如果为false，就会抛出
		// java.lang.IllegalStateException异常。
		// bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
		setBackground(new BitmapDrawable(bitmap));
		// cacheBitmap = bitmap;
		// cacheCanvas.setBitmap(cacheBitmap);
		// 刷新页面
		invalidate();
	}

	/**
	 * 清空画布
	 */
	public void clear() {
		// 创建一个和DrawView大小相同的透明画布
		cacheBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(),
				Config.ARGB_8888);
		cacheCanvas = new Canvas();
		// 设置cacheCanvas将会绘制到内存中的cacheBitmap上
		cacheCanvas.setBitmap(cacheBitmap);
		// 刷新页面
		invalidate();
	}

	/**
	 * 设置画笔
	 * 
	 * @param color
	 *            设置画笔的颜色
	 * @param width
	 *            画笔宽度
	 * 
	 */
	public void setPaint(int color, int width) {
		// 设置画笔的颜色
		paint = new Paint(Paint.DITHER_FLAG);
		paint.setColor(color);
		// 设置画笔风格
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		// 反锯齿
		paint.setAntiAlias(true);
		paint.setDither(true);

		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);

		flag_earser = false;
	}

	/**
	 * 设置激光笔状态
	 * 
	 * @param laser
	 *            是否处于激光笔状态
	 */
	public void setLaser(boolean laser) {
		this.flag_laser = laser;
	}

	public void setLaser2(boolean laser) {
		this.flag_laser2 = laser;
	}

	/**
	 * 设置橡皮擦
	 */
	public void setEarser() {
		// 设置橡皮擦画笔
		Paint cPaint = new Paint();
		cPaint.setAlpha(0);
		cPaint.setColor(Color.TRANSPARENT);
		cPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		cPaint.setAntiAlias(true);
		cPaint.setDither(true);
		cPaint.setStyle(Paint.Style.STROKE);
		cPaint.setStrokeJoin(Paint.Join.ROUND);
		cPaint.setStrokeCap(Paint.Cap.ROUND);
		cPaint.setStrokeWidth(60);
		paint = cPaint;
		clear_bitmap = Bitmap.createScaledBitmap(clear_bitmap, 60, 60, true);
		flag_earser = true;
	}

	/**
	 * 根据手指移动进行绘图
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取拖动事件的发生位置
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 手按下
			flag_up = false;
			path.moveTo(x, y);
			if (flag_laser) {
				// 记录画笔点的信息
				listPointInfos.add(new PointInfo(x, y, System
						.currentTimeMillis(), new Paint(paint), true, false));
			}
			preX = x;
			preY = y;
			break;
		case MotionEvent.ACTION_MOVE: // 手移动
			path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
			if (flag_laser) {
				// 记录画笔点的信息
				listPointInfos.add(new PointInfo(x, y, System
						.currentTimeMillis(), new Paint(paint), false, false));
			}
			preX = x;
			preY = y;
			break;
		case MotionEvent.ACTION_UP: // 手放开
			flag_up = true;
			if (flag_laser && (!flag_earser)) {
				// 记录点信息
				listPointInfos.add(new PointInfo(x, y, System
						.currentTimeMillis(), new Paint(paint), false, true));
				new RefreshThread().start();
			} else if (!flag_laser2) {
				cacheCanvas.drawPath(path, paint);
			} else if (flag_laser2 && (!flag_earser)) {
				// 拷贝path对象加到list
				listPathInfos.add(new PathInfo(new Path(path),
						new Paint(paint), System.currentTimeMillis()));
				new RefreshThread().start();
			}
			path.reset();
			break;
		}
		invalidate();
		// 返回true表明处理方法已经处理该事件
		return true;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		Paint bmpPaint = new Paint();
		// 将cacheBitmap绘制到该View组件上
		canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
		// 如果是在橡皮擦状态则画橡皮擦图标
		if (flag_earser) {
			if (!flag_up) {
				canvas.drawBitmap(clear_bitmap, preX - clear_bitmap.getWidth()
						/ 2, preY - clear_bitmap.getHeight() / 2, null);
			}
			// 沿着path绘制
			// 这里使用cacheCanvas是防止Xfermode绘图出现绘图时黑框
			cacheCanvas.drawPath(path, paint);
		} else if (!flag_laser) {
			// 沿着path绘制,没有这行不会动态绘制
			canvas.drawPath(path, paint);
		}
		if (flag_laser) {
			float mPreX = 0;
			float mPreY = 0;
			Path mPath = new Path();
			for (int i = 0; i < listPointInfos.size(); i++) {
				PointInfo pointInfo = listPointInfos.get(i);
				if (System.currentTimeMillis() - pointInfo.getTime() > 2000) {
					listPointInfos.remove(i);
					if (i + 1 < listPointInfos.size()) {
						listPointInfos.get(i + 1).setBegin(true);
					}
					continue;
				}
				if (pointInfo.isBegin()) {
					mPath.moveTo(pointInfo.getX(), pointInfo.getY());
					mPreX = pointInfo.getX();
					mPreY = pointInfo.getY();
				} else {
					mPath.quadTo(mPreX, mPreY, (pointInfo.getX() + mPreX) / 2,
							(pointInfo.getY() + mPreY) / 2);
					mPreX = pointInfo.getX();
					mPreY = pointInfo.getY();
					canvas.drawPath(mPath, paint);
				}
			}
		}
		if (flag_laser2) {// 激光笔状态处理
			for (int i = 0; i < listPathInfos.size(); i++) {
				PathInfo pathInfo = listPathInfos.get(i);
				long t = System.currentTimeMillis() - pathInfo.getTime();
				if (2100 > t && t > 2000) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(240);
				} else if (2200 > t && t >= 2100) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(230);
				} else if (2300 > t && t >= 2200) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(220);
				} else if (2400 > t && t >= 2300) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(210);
				} else if (2500 > t && t >= 2400) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(200);
				} else if (2600 > t && t >= 2500) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(190);
				} else if (2700 > t && t >= 2600) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(180);
				} else if (2800 > t && t >= 2700) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(170);
				} else if (2900 > t && t >= 2800) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(160);
				} else if (3000 > t && t >= 2900) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(150);
				} else if (3100 > t && t >= 3000) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(140);
				} else if (3200 > t && t >= 3100) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(130);
				} else if (3300 > t && t >= 3200) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(120);
				} else if (3400 > t && t >= 3300) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(110);
				} else if (3500 > t && t >= 3400) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(100);
				} else if (3600 > t && t >= 3500) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(90);
				} else if (3700 > t && t >= 3600) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(80);
				} else if (3800 > t && t >= 3700) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(70);
				} else if (3900 > t && t >= 3800) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(60);
				} else if (4000 > t && t >= 3900) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(50);
				} else if (4100 > t && t >= 4000) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(40);
				} else if (4200 > t && t >= 4100) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(30);
				} else if (4300 > t && t >= 4200) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(20);
				} else if (4400 > t && t >= 4300) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(10);
				} else if (t >= 4400) {// 画笔时长超过2秒清理掉
					pathInfo.getmPaint().setAlpha(0);
//					listPathInfos.remove(i);
//					continue;
				}
				// 每次都要用临时画笔绘制激光笔路径
				canvas.drawPath(pathInfo.getmPath(), pathInfo.getmPaint());
			}
		}
	}

	public class RefreshThread extends Thread {
		@Override
		public void run() {
			long time = System.currentTimeMillis();
			super.run();
			while (System.currentTimeMillis() - time < 5000) {
				try {
					postInvalidate();
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
