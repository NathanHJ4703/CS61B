package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Rooms {
    private int side;
    private Position upperLeftPos;
    private TETile tile;
    private static final Random RANDOM = new Random();
    private int width;
    private int height;

    public Rooms(Position upperLeft, int width, int height, TETile tile) {
        this.width = width;
        this.height = height;
        upperLeftPos = upperLeft;
        this.tile = tile;
    }

    public TETile getTile() {
        return tile;
    }

    public void drawRooms() {
        // initialize tiles
//        int WIDTH = RANDOM.nextInt();
//        int HEIGHT = RANDOM.nextInt();
//        TETile[][] room = new TETile[WIDTH][HEIGHT];
//        for (int x = upperLeft.getX(); x < WIDTH + upperLeft.getX(); x += 1) {
//            for (int y = upperLeft.getY(); y < HEIGHT + upperLeft.getY(); y += 1) {
//                if (x == upperLeft.getX() || x == WIDTH + upperLeft.getX()
//                        || y == upperLeft.getY() || y == HEIGHT + upperLeft.getY()) {
//                    room[x][y] = Tileset.WALL;
//                }
//                room[x][y] = Tileset.FLOOR;
//            }
//        }
        int WIDTH = RANDOM.nextInt();
        int HEIGHT = RANDOM.nextInt();
        TETile[][] room = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (x == 0 || x == (WIDTH - 1) || y == 0 || y == (HEIGHT - 1)) {
                    room[x][y] = Tileset.WALL;
                }
                room[x][y] = Tileset.FLOOR;
            }
        }
    }

    /** Returns a list of the (x,y) positions that this room covers */
    public List<Position> getCoordinates() {
        List<Position> list = new LinkedList<>();
// fix this one to match behavior of rectangles
        int WIDTH = RANDOM.nextInt();
        int HEIGHT = RANDOM.nextInt();
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (x == 0 || x == (WIDTH - 1) || y == 0 || y == (HEIGHT - 1)) {
                    list.add(new Position(x, y, Tileset.WALL));
                }
                list.add(new Position(x, y, Tileset.FLOOR));
            }
        }
        return list;
    }

    //not sure if right. Might delete it because of no usage in getCoordinates.

    public void overlap() {

    }
}