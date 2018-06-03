package com.khanhhua.battleship.commons;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
