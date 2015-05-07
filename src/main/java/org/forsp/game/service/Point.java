package org.forsp.game.service;

/**
 * @author Serhii Kryvtsov
 * @since 04/05/2015
 */
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("x=%s, y=%s", x, y);
    }
}
