package byow.Core;

import byow.TileEngine.TETile;

public class Position {
    private int x;
    private int y;
    private TETile tile;

    public Position(int x, int y, TETile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }
    public int getX(){
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public TETile getTile() {
        return tile;
    }
}
