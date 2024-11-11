package org.example;

public class Circle {
    public double bend;
    public Complex center;
    public double radius;

    public Circle(double bend, double x, double y) {
        this.bend = bend;
        this.center = new Complex(x, y);
        this.radius = Math.abs(1 / bend);
    }

    public double dist(Circle other) {
        return distance(center.a, center.b, other.center.a, other.center.b);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
