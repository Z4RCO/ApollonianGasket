package org.example;

import static java.lang.Math.atan2;

public class Complex {
    public double a;
    public double b;

    Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public Complex add(Complex other) {
        return new Complex(this.a + other.a, this.b + other.b);
    }

    public Complex sub(Complex other) {
        return new Complex(this.a - other.a, this.b - other.b);
    }

    public Complex scale(double value) {
        return new Complex(this.a * value, this.b * value);
    }

    public Complex mult(Complex other) {
        var a = this.a * other.a - this.b * other.b;
        var b = this.a * other.b + other.a * this.b;
        return new Complex(a, b);
    }

    public Complex sqrt() {
        // Convert to polar form
        var m = Math.sqrt(this.a * this.a + this.b * this.b);
        var angle = atan2(this.b, this.a);
        // Calculate square root of magnitude and use half the angle for square root
        m = Math.sqrt(m);
        angle = angle / 2;
        // Back to rectangular form
        return new Complex(m * Math.cos(angle), m * Math.sin(angle));
    }
}
