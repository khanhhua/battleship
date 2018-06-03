package com.khanhhua.battleship.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Grid implements Serializable {
    int size;
    Ship[] array;
    boolean[] shots;

    Grid(int size) {
        this.size = size;
        this.array = new Ship[size * size];
        this.shots = new boolean[size * size];

        for (int i = 0; i < size * size; i++) {
            this.array[i] = null;
            this.shots[i] = false;
        }
    }

    void registerShipAt(int x, int y, Ship ship) {
        this.array[y * this.size + x] = ship;
    }

    void putShotAt(int x, int y) {
        this.shots[y * this.size + x] = true;
    }

    int countShips(boolean liveOnly) {
        int count = 0;
        HashMap<Ship, Integer> shipCounts = new HashMap<Ship, Integer>();

        for (int i = 0, len = this.array.length; i < len; i++) {
            Ship ship;
            if (this.array[i] != null && (!liveOnly || (liveOnly && this.shots[i] == false))) {
                ship = this.array[i];
                if (shipCounts.containsKey(ship)) {
                    shipCounts.put(ship, shipCounts.get(ship) + 1);
                } else {
                    shipCounts.put(ship, 1);
                }
            }
        }

        for (Map.Entry<Ship, Integer> entry : shipCounts.entrySet()) {
            if (entry.getValue() > 0) {
                count += 1;
            }
        }

        return count;
    }

    Ship identify(int x, int y) {
        return array[y * size + x];
    }
}
