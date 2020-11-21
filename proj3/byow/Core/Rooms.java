package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Rooms {
    private int side;
    private Position upperLeft;
    private TETile tile;
    private static final Random RANDOM = new Random();

    public Rooms(Position upperLeft, int WIDTH, int HEIGHT) {

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
    public List<Position> getCoordinates() {
        List<Position> list = new LinkedList<>();
// fix this one to match behavior of rectangles
        for (int i = 0; i < side; i++) {
            int x = upperLeft.getX() - i;
            int y = upperLeft.getY() - i;
            addRow(list, new Position(x, y), side + i * 2);
        }

        return list;

    }

//not sure if right
    public void addRow(List<Position> list, Position startPosition, int length) {
        for (int offset = 0; offset < length; offset ++) {
            int y = startPosition.getY();
            int x = startPosition.getX() + offset;
            list.add(new Position(x, y));
        }
    }
    public void overlap() {

    }
}

