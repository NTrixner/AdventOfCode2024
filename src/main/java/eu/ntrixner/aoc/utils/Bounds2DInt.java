package eu.ntrixner.aoc.utils;

public class Bounds2DInt {
    public int left, top, right, bottom;

    public Bounds2DInt(int left, int right, int top, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean isInBounds(int x, int y) {
        return x >= left && x < right && y >= top && y < bottom;
    }
}
