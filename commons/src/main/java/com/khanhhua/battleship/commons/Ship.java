package com.khanhhua.battleship.commons;

import java.util.Arrays;

/**
 * Ship class
 */
public class Ship {
    public static Ship create(ShipType type) {
        Ship ship = new Ship();

        int hitPoints = type.getHitPoints();
        ship.hitPoints = hitPoints;
        ship.name = type.getName();
        ship.hits = new boolean[hitPoints];
        Arrays.fill(ship.hits, Boolean.FALSE);

        return ship;
    }

    private boolean[] hits;
    private int hitPoints;
    private String name;

    ShipOrientation orientation;
    int x;
    int y;

    public boolean[] getHits() {
        return hits;
    }

    public void hit(int hitAt) {
        this.hits[hitAt] = true;
    }

    public String getName() {
        return name;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void locate(int x, int y, ShipOrientation orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }
}
