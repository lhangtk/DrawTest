package com.example.drawtest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {
    public Paint paint = null;
    // 定义一个内存中的图片，该图片将作为缓冲区
    private Bitmap cacheBitmap = null;
    // 定义cacheBitmap上的Canvas对象
    private Canvas cacheCanvas = null;

    private List<PressurePoint> pressurePoints;
    PressurePoint pressurePoint = new PressurePoint();

    private boolean flag_up = true;

    private int step = 70;
    private final int MAX_STEP = 70;

    public boolean laser = false;

    private Bitmap laserBitmap;

    private boolean hasSave = false;

    public DrawView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    private void init() {
        pressurePoints = new ArrayList<PressurePoint>();
    }

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
        // 设置cacheCanvas将会绘制到内存中的cacheBitmap上
        cacheCanvas.setBitmap(cacheBitmap);
        setPaint(Color.RED, 5);

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
    }

    /**
     * 设置画笔
     *
     * @param color 设置画笔的颜色
     * @param width 画笔宽度
     */
    public void setPaint(int color, int width) {
        // 设置画笔的颜色
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(color);
        // 设置画笔风格
        paint.setStyle(Paint.Style.FILL);//实心
        paint.setStrokeWidth(5);
        // 反锯齿
        paint.setAntiAlias(true);
        paint.setDither(true);

        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 根据手指移动进行绘图
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (laser&&!hasSave){
            laserBitmap = cacheBitmap;
            hasSave = true;
        }
        if (!laser){
            hasSave = false;
        }
        setPenSize(1f, 12);
        // 获取拖动事件的发生位置
//        float x = event.getX();
//        float y = event.getY();
//        int N = event.getHistorySize();
//        int P = event.getPointerCount();
//        int k = event.getActionIndex();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 手按下
                reset();
//                pressurePoint.x = x;
//                pressurePoint.y = y;
//                pressurePoint.size = event.getSize();
//                pressurePoint.pressure = event.getPressure();
//                pressurePoint.tool = getToolTypeCompat(event,k);
                addPoint(event);
                flag_up = false;
//                path.moveTo(x, y);
//                preX = x;
//                preY = y;
                break;
            case MotionEvent.ACTION_MOVE: // 手移动
//                pressurePoint.x = x;
//                pressurePoint.y = y;
//                pressurePoint.size = event.getSize();
//                pressurePoint.pressure = event.getPressure();
//                pressurePoint.tool = getToolTypeCompat(event,k);
                addPoint(event);
//                path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
//                preX = x;
//                preY = y;
                break;
            case MotionEvent.ACTION_UP: // 手放开
                flag_up = true;
//                cacheCanvas.drawPath(path, paint);
//                path.reset();
//                List<PressurePoint> points = new ArrayList<PressurePoint>();
//                points.addAll(pressurePoints);
//                laserPoints.add(new LaserPoint(255,points));
                break;
        }
        if (laser&&step>=MAX_STEP){
            step = 0;
            laserHandler.sendEmptyMessage(1);
        }
        if (laser&&step<MAX_STEP){
            step = 0;
        }
        invalidate();
        // 返回true表明处理方法已经处理该事件
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!flag_up) {
            drawLine(canvas);
        } else {
            Paint bmpPaint = new Paint();
            // 将cacheBitmap绘制到该View组件上
            canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
        }
    }

    private float mPressureExponent = 2.0f;

    private float mRadiusMin;
    private float mRadiusMax;
    private PressureCooker mPressureCooker = new PressureCooker(getContext());
    public static final boolean ASSUME_STYLUS_CALIBRATED = true;
    private float mLastR = -1, mLastLen = 0, mLastX = 0, mLastY = 0;

    private void drawLaser() {
        if (!laser){
            Bitmap b = cacheBitmap;
            clear();
            Paint paint1 = new Paint();
            paint1.setAlpha(240);
            cacheCanvas.drawBitmap(laserBitmap,0,0,new Paint());
            cacheCanvas.drawBitmap(b, 0, 0, paint1);
            invalidate();
            step = MAX_STEP;
            return;
        }
        Bitmap b = cacheBitmap;
        clear();
        Paint paint1 = new Paint();
        paint1.setAlpha(240);
        cacheCanvas.drawBitmap(laserBitmap,0,0,new Paint());
        cacheCanvas.drawBitmap(b, 0, 0, paint1);
        invalidate();
        if (step++ < MAX_STEP) {
            laserHandler.sendEmptyMessageDelayed(1, 100);
        }
    }

    Handler laserHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            drawLaser();
        }
    };

    private void drawLine(Canvas canvas) {
        Paint bmpPaint = new Paint();
//        // 将cacheBitmap绘制到该View组件上
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
        for (PressurePoint pressurePoint:pressurePoints){
//        if (pressurePoints.size()>0) {
//            PressurePoint pressurePoint = pressurePoints.get(pressurePoints.size()-1);
        final float pressureNorm;

        if (ASSUME_STYLUS_CALIBRATED && pressurePoint.tool == MotionEvent.TOOL_TYPE_STYLUS) {
            pressureNorm = pressurePoint.pressure;
        } else {
            pressureNorm = mPressureCooker.getAdjustedPressure(pressurePoint.pressure);
        }
        final float radius = lerp(mRadiusMin, mRadiusMax,
                (float) Math.pow(pressureNorm, mPressureExponent));
        strokeTo(cacheCanvas, pressurePoint.x, pressurePoint.y, radius);
//            canvas.drawCircle(pressurePoint.x,pressurePoint.y,5,paint);
//        }
        }
//        reset();
    }

    public void setPenSize(float min, float max) {
        mRadiusMin = min * 0.5f;
        mRadiusMax = max * 0.5f;
    }

    private void addPoint(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int k = event.getActionIndex();
        int N = event.getHistorySize();
//        int P = event.getPointerCount();
        pressurePoints.clear();
        int j = 0;
        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < P; j++) {
                PressurePoint pressurePoint = new PressurePoint();
                pressurePoint.x = event.getHistoricalX(j, i);
                pressurePoint.y = event.getHistoricalY(j, i);
                pressurePoint.size = event.getHistoricalSize(j, i);
                pressurePoint.pressure = event.getHistoricalPressure(j, i) + event.getHistoricalSize(j, i);
                pressurePoint.time = event.getHistoricalEventTime(i);
                pressurePoint.tool = getToolTypeCompat(event, j);
                pressurePoints.add(pressurePoint);
//            }
        }
//        for (int j = 0; j < P; j++) {
//            PressurePoint pressurePoint = new PressurePoint();
//            pressurePoint.x = event.getX(j);
//            pressurePoint.y = event.getY(j);
//            pressurePoint.size = event.getSize(j);
//            pressurePoint.pressure = event.getPressure(j) + event.getSize(j);
//            pressurePoint.time = event.getEventTime();
//            pressurePoint.tool = getToolTypeCompat(event, j);
//            pressurePoints.add(pressurePoint);
//        }
//        PressurePoint pressurePoint = new PressurePoint();
//        pressurePoint.x = x;
//        pressurePoint.y = y;
//        pressurePoint.size = event.getSize();
//        pressurePoint.pressure = event.getPressure();
//        pressurePoint.tool = getToolTypeCompat(event,k);
//        pressurePoints.add(pressurePoint);
    }

    public void strokeTo(Canvas c, float x, float y, float r) {
        if (mLastR < 0) {
            // always draw the first point
            drawStrokePoint(c, x, y, r);
        } else {
            // connect the dots, la-la-la

            mLastLen = dist(mLastX, mLastY, x, y);
            float xi, yi, ri, frac;
            float d = 0;
            while (true) {
                if (d > mLastLen) {
                    break;
                }
                frac = d == 0 ? 0 : (d / mLastLen);
                ri = lerp(mLastR, r, frac);
                xi = lerp(mLastX, x, frac);
                yi = lerp(mLastY, y, frac);
                drawStrokePoint(c, xi, yi, ri);

                // for very narrow lines we must step (not much more than) one radius at a time
                final float MIN = 1f;
                final float THRESH = 16f;
                final float SLOPE = 0.1f; // asymptote: the spacing will increase as SLOPE*x
                if (ri <= THRESH) {
                    d += MIN;
                } else {
                    d += Math.sqrt(SLOPE * Math.pow(ri - THRESH, 2) + MIN);
                }
            }
        }

        mLastX = x;
        mLastY = y;
        mLastR = r;
    }

    public void reset() {
        mLastX = mLastY = 0;
        mLastR = -1;
    }

    final void drawStrokePoint(Canvas c, float x, float y, float r) {
        c.drawCircle(x, y, r, paint);
    }

    final float dist(float x1, float y1, float x2, float y2) {
        x2 -= x1;
        y2 -= y1;
        return (float) Math.sqrt(x2 * x2 + y2 * y2);
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    final static int getToolTypeCompat(MotionEvent me, int index) {
        if (hasToolType()) {
            return me.getToolType(index);
        }

        // dirty hack for the HTC Flyer
        if ("flyer".equals(Build.HARDWARE)) {
            if (me.getSize(index) <= 0.1f) {
                // with very high probability this is the stylus
                return MotionEvent.TOOL_TYPE_STYLUS;
            }
        }

        return MotionEvent.TOOL_TYPE_FINGER;
    }

    final static boolean hasToolType() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH);
    }
}