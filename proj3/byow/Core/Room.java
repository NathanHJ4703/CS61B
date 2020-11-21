package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Room {
    private int side;
    private Position bottomLeft;
    private TETile tile;
    private static final Random RANDOM = new Random();
    private int width;
    private int height;


    public Room(Position position, int width, int height) {
        bottomLeft = position;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TETile getTile() {
        return tile;
    }

    public void drawRoom() {
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

    // Returns a list of all the wall coordinates for the room.
    public List<Position> getWallCoordinates() {
        List<Position> wallCoordinates = new LinkedList<>();
        int topSide = bottomLeft.getY() + height;
        while (topSide > OurWorld.getYDimension()) {
            topSide--;
        }
        int rightSide = bottomLeft.getX() + width;
        while (rightSide > OurWorld.getXDimension()) {
            rightSide--;
        }
        for (int x = bottomLeft.getX(); x < rightSide; x += 1) {
            for (int y = bottomLeft.getY(); y < topSide; y += 1) {
                if (x == bottomLeft.getX() || x == (rightSide - 1) || y == bottomLeft.getY() || y == (topSide - 1)) {
                    wallCoordinates.add(new Position(x, y));
                }
            }
        }

        return wallCoordinates;
    }

    // returns a list of all the floor coordinates for the room.
    public List<Position> getFloorCoordinates() {
        List<Position> floorCoordinates = new LinkedList<>();
        int topSide = bottomLeft.getY() + height;
        while (topSide > OurWorld.getYDimension()) {
            topSide--;
        }
        int rightSide = bottomLeft.getX() + width;
        while (rightSide > OurWorld.getXDimension()) {
            rightSide--;
        }
        for (int x = bottomLeft.getX(); x < rightSide; x += 1) {
            for (int y = bottomLeft.getY(); y < topSide; y += 1) {
                if (x != bottomLeft.getX() && x != (rightSide - 1) && y != bottomLeft.getY() && y != (topSide - 1)) {
                    floorCoordinates.add(new Position(x, y));
                }
            }
        }

        return floorCoordinates;
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


    public static void main(String[] args) {

    }

}
