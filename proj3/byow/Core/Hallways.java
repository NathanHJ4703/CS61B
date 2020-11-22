package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class Hallways extends Room {
    private int length;
    private Position p;
    private static final int WIDTH = 3;
    private static final Random RANDOM = new Random();
    private Room start;

    public Hallways(Position p, int length, Room start) {
        super(p, WIDTH, length);
    }


    public void drawVerticalHallway() {
        //static x
        int length = RANDOM.nextInt();
        TETile[][] vertHall = new TETile[WIDTH][length];
        for (int x = 0; x <= WIDTH; x++) {
            for (int y = 0; y < length; y++) {
                if (x == 0 || x == WIDTH || y == 0 || y == length - 1) {
                    vertHall[x][y] = Tileset.WALL;
                }
                vertHall[x][y] = Tileset.FLOOR;
            }
        }
    }


    public void drawHorizontalHallway() {
        //static y
        int length = RANDOM.nextInt();
        TETile[][] horizHall = new TETile[length][WIDTH];
        for (int y = 0; y <= WIDTH; y++) {
            for (int x = 0; x < length; x++) {
                if (y == 0 || y == WIDTH || x == 0 || x == length - 1) {
                    horizHall[x][y] = Tileset.WALL;
                }
                horizHall[x][y] = Tileset.FLOOR;
            }
        }
    }

    public List<Position> getWallCoordinates() {
        return null;
    }

    public List<Position> getFloorCoordinates() {
        return null;
    }

    public void drawCorner() {

    }

}