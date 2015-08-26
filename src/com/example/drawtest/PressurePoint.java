package com.example.drawtest;

/**
 * Created by hangli2 on 2015/7/17.
 */
public class PressurePoint {
    public float x,y;
    public float size, pressure;
    public long time; // ms, in SystemClock.currentThreadTimeMillis base
    public int tool;

    public PressurePoint(float x, float y, float size, float pressure, long time, int tool) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.pressure = pressure;
        this.time = time;
        this.tool = tool;
    }

    public PressurePoint() {
        super();
    }
}
