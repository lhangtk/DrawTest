package com.example.drawtest;

import java.util.List;

/**
 * Created by hangli2 on 2015/7/20.
 */
public class LaserPoint {
    public int alpha;
    public List<PressurePoint> pressurePoints;

    public LaserPoint(int alpha, List<PressurePoint> pressurePoints) {
        this.alpha = alpha;
        this.pressurePoints = pressurePoints;
    }
}
