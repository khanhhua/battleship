package com.khanhhua.battleship.commons;

import java.io.Serializable;
import java.util.Date;

public class Game implements Serializable {
    static final int SHIP_COUNT = 7;

    public static final int SHIP_1 = 0;
    public static final int SHIP_2 = 1;
    public static final int SHIP_3 = 2;
    public static final int SHIP_4 = 3;
    public static final int SHIP_5 = 4;
    public static final int SHIP_6 = 5;
    public static final int SHIP_7 = 6;

    static final int ORIENTATION_HORIZONTAL = 1;
    static final int ORIENTATION_VERTICAL = 2;

    public static final int STATUS_PENDING = 1;
    public static final int STATUS_READY = 2;
    public static final int STATUS_PLAYING = 3;
    public static final int STATUS_COMPLETE = 4;

    private long id;
    private boolean owned;
    private int size;
    private int status;
    private Player owner;
    private Player opponent;
    /**
     * Our fleet's arrangement
     */
    private Grid primary;
    /**
     * Opponent's fleet's arrangement
     */
    private Grid tracker;

    private Ship[] ships = new Ship[SHIP_COUNT];
    private Ship unknown = Ship.create(ShipType.UNKNOWN);

    public Game(Player owner, int size) {
        this.id = new Date().getTime();
        this.size = size;
        this.primary = new Grid(size);
        this.tracker = new Grid(size);

        this.ships[SHIP_1] = Ship.create(ShipType.DESTROYER);
        this.ships[SHIP_2] = Ship.create(ShipType.DESTROYER);
        this.ships[SHIP_3] = Ship.create(ShipType.SUBMARINE);
        this.ships[SHIP_4] = Ship.create(ShipType.CRUISER);
        this.ships[SHIP_5] = Ship.create(ShipType.CRUISER);
        this.ships[SHIP_6] = Ship.create(ShipType.BATTLESHIP);
        this.ships[SHIP_7] = Ship.create(ShipType.CARRIER);

        this.owner = owner;
        this.owned = true;
    }

    public Game() {
        this.ships[SHIP_1] = Ship.create(ShipType.DESTROYER);
        this.ships[SHIP_2] = Ship.create(ShipType.DESTROYER);
        this.ships[SHIP_3] = Ship.create(ShipType.SUBMARINE);
        this.ships[SHIP_4] = Ship.create(ShipType.CRUISER);
        this.ships[SHIP_5] = Ship.create(ShipType.CRUISER);
        this.ships[SHIP_6] = Ship.create(ShipType.BATTLESHIP);
        this.ships[SHIP_7] = Ship.create(ShipType.CARRIER);

        this.owned = false;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public boolean isOwned() {
        return owned;
    }

    void setOwned(boolean owned) {
        this.owned = owned;
    }

    public int getSize() {
        return size;
    }

    void setSize(int size) {
        this.size = size;
    }
    /**
     * Ship anchor point is the bow (front)
     *
     * @param x
     * @param y
     * @param shipID
     * @param orientation
     */
    public void putShipAt(int x, int y, int shipID, int orientation) {
        if (x < 0 || x >= size) {
            throw new IllegalArgumentException("Coordinate out of range");
        }

        if (y < 0 || y >= size) {
            throw new IllegalArgumentException("Coordinate out of range");
        }

        if (shipID < 0 || shipID >= ships.length) {
            throw new IllegalArgumentException("ShipID out of range");
        }

        if (orientation != ORIENTATION_HORIZONTAL && orientation != ORIENTATION_VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation");
        }

        Ship ship = ships[shipID];
        int initialHits = ship.getHitPoints();
        if (orientation == ORIENTATION_HORIZONTAL) {
            for (int i = 0; i < initialHits; i++) {
                if (x + i >= size) {
                    throw new IllegalArgumentException("Coordinate out of range");
                }

                primary.registerShipAt(x + i, y, ship);
            }
        } else {
            for (int i = 0; i < initialHits; i++) {
                if (y + i >= size) {
                    throw new IllegalArgumentException("Coordinate out of range");
                }

                primary.registerShipAt(x, y + i, ship);
            }
        }

        ship.locate(x, y, orientation == ORIENTATION_HORIZONTAL?ShipOrientation.HORIZONTAL:ShipOrientation.VERTICAL);
    }

    /**
     * Opponent agent asks "Is it a hit at (X, Y)?"
     * If yes,
     *
     * @param x
     * @param y
     * @return
     */
    public boolean verifyHit(int x, int y) {
        Ship ship = primary.identify(x, y);
        if (ship == null) {
            return false;
        }

        if (ship.orientation == ShipOrientation.HORIZONTAL) {
            ship.hit(x - ship.x);
        } else {
            ship.hit(y - ship.y);
        }

        return true;
    }

    /**
     * After asking the opponent "Is it a hit at (X, Y)?", if the answer is yes, hit is true...
     *
     * @param x
     * @param y
     * @param hit {boolean}
     */
    public void mark(int x, int y, boolean hit) {
        tracker.putShotAt(x, y);

        if (hit) {
            tracker.registerShipAt(x, y, unknown);
        }
    }

    public Player getOwner() {
        return owner;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOppoent(Player player) {
        this.opponent = player;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
