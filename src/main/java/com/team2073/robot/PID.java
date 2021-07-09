package com.team2073.robot;

public class PID {
    private double p;
    private double i;
    private double d;
    private double referencePoint;
    private double error;
    private double acculmativeError;
    private double output;
    double previousError = 0;

    public PID(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
    }

    public void set(double referencePoint) {
        this.referencePoint = referencePoint;
    }

    public void calculateOutput(double currentPoint) {
        output = 0;
        error = referencePoint - currentPoint;
        double m;
        m = (error - previousError) / 0.01;
        acculmativeError += error * 0.01;
        output += p * error;
        output += d * m;
        output += i * acculmativeError;
        previousError = error;
    }

    public double getOutput() {
        return output;
    }

}
