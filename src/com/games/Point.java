package com.games;

/**
 * Created with IntelliJ IDEA.
 * User: Binnie
 * Date: 28/10/12
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */
public class Point {
    private int x, y;

    public Point(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public Point(){}

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Point) {
            Point that = (Point) other;
            result = (this.getX() == that.getX() && this.getY() == that.getY());
        }
        return result;
    }

    @Override public int hashCode() {
        return (41 * (41 + getX()) + getY());
    }

}