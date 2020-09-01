package net.codebot.shapes;

import java.util.HashMap;

public class Floor {
    private int [][] grid;
    private HashMap<String,String> testMap;

    public Floor(int [][] grid){
        this.grid = grid;
    }

    public int[][] getGrid() {
        return this.grid;
    }
}
