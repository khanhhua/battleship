package com.khanhhua.battleship.commons;

import java.io.Serializable;

public class ShipPosition implements Serializable {
    private int x;
    private int y;

    private int orientation;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
