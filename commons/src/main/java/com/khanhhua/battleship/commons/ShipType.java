package com.khanhhua.battleship.commons;

/**
 * ShipType enumeration
 */
public enum ShipType {
    UNKNOWN(1, "Unknown"),
    DESTROYER(2, "Destroyer"),
    SUBMARINE(3, "Submarine"),
    CRUISER(3, "Cruiser"),
    BATTLESHIP(4, "Battleship"),
    CARRIER(5, "Carrier")
    ; //
    private int hitPoints;
    private String name;

    ShipType(int hitPoints, String name) {
        this.hitPoints = hitPoints;
        this.name = name;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public String getName() {
        return name;
    }
}
