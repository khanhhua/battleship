package com.khanhhua.battleship.commons;

import java.io.Serializable;

public class Shot implements Serializable {
    private int x;
    private int y;

    public Shot() {}

    public Shot(int x, int y) {
        this.x = x;
        this.y = y;
    }

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
}
