package com.example.burakkara.gameoflife;

import java.io.Serializable;

/**
 * Author: karab on 30/06/15.
 */
public class GameBundle implements Serializable {
    private int[][] cells1;

    public GameBundle(int[][] cells1) {
        this.cells1 = cells1;
    }

    public int[][] getCells1() {
        return cells1;
    }
}
