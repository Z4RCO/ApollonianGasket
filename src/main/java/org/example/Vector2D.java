package org.example;

public class Vector2D {
    public double x;
    public double y;

    // Constructor using Cartesian coordinates
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Constructor using magnitude and angle (in radians)
    public Vector2D(double magnitude, double angle, boolean isRadians) {
        if (!isRadians) {
            // Convert degrees to radians if needed
            angle = Math.toRadians(angle);
        }
        this.x = magnitude * Math.cos(angle);
        this.y = magnitude * Math.sin(angle);
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Setters
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Method to calculate the magnitude (length) of the vector
    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Method to calculate the angle (in radians) of the vector
    public double getAngleRadians() {
        return Math.atan2(y, x);
    }

    // Method to calculate the angle (in degrees) of the vector
    public double getAngleDegrees() {
        return Math.toDegrees(getAngleRadians());
    }

    // Method to add another vector
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    // Method to subtract another vector
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    // Method to scale the vector by a scalar value
    public Vector2D scale(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    // Method to calculate the dot product with another vector
    public double dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    // Method to get the unit vector (normalized vector)
    public Vector2D normalize() {
        double magnitude = getMagnitude();
        return new Vector2D(this.x / magnitude, this.y / magnitude);
    }


    public void rotate(double angle) {

        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        double newX = x * cosAngle - y * sinAngle;
        double newY = x * sinAngle + y * cosAngle;

        this.x = newX;
        this.y = newY;
    }

    // Method to set the magnitude (length) of the vector
    public void setMagnitude(double newMagnitude) {
        double currentMagnitude = getMagnitude();
        if (currentMagnitude != 0) {
            this.x = (x / currentMagnitude) * newMagnitude;
            this.y = (y / currentMagnitude) * newMagnitude;
        } else {
            this.x = newMagnitude;
            this.y = 0;
        }
    }

}
