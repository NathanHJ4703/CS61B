package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab12.Position;

import java.util.LinkedList;
import java.util.List;

public class Room {
    private int width;
    private int height;
    private Position upperLeftPos;
    private TETile tile;

    public Room(int width, int height, Position upperLeft, TETile tile) {
        this.width = width;
        this.height = height;
        upperLeftPos = upperLeft;
        this.tile = tile;
    }

    /** Returns a list of the (x,y) positions that this room covers */
    public List<byow.Core.Position> getCoordinates() {
        List<byow.Core.Position> list = new LinkedList<>();
// fix this one to match behavior of rectangles
        int WIDTH = RANDOM.nextInt();
        int HEIGHT = RANDOM.nextInt();
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (x == 0 || x == (WIDTH - 1) || y == 0 || y == (HEIGHT - 1)) {
                    list.add(new byow.Core.Position(x, y, Tileset.WALL));
                }
                list.add(new byow.Core.Position(x, y, Tileset.FLOOR));
            }
        }
        return list;
    }

    public TETile getTile() {
        return tile;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getUpperLeftPos() {
        return upperLeftPos;
    }

    public static void main(String[] args) {

    }

}
