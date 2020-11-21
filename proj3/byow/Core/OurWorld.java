package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OurWorld {
    //Maximum dimensions for our world.
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    //Maximum width and height for the rooms.
    private static final int maxWidth = WIDTH/2;
    private static final int maxHeight = HEIGHT/2;
    //The maximum limit of which x and y values the bottom left corner of the room can go in order to prevent the problem of out of bounds.
    private static final int largestX = WIDTH - 3;
    private static final int largestY = HEIGHT - 3;
    public static Set<Position> coveredPositions = new HashSet<>();
    public static Set<Room> distinctRooms = new HashSet<>();

    public static int getYDimension() {
        return HEIGHT;
    }

    public static int getXDimension() {
        return WIDTH;
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] ourWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                ourWorld[x][y] = Tileset.NOTHING;
            }
        }
        int i = 10;
        Random rand = new Random();
        while(i > 0) {
            Room r = new Room(new Position(rand.nextInt(largestX+1), rand.nextInt(largestY+1)),
                    rand.nextInt(maxWidth+1) + 3, rand.nextInt(maxHeight+1) + 3);
            addRoom(r, ourWorld);
            i--;
        }
        //Room r = new Room(new Position(largestX, largestY), 30, 15);
        //addRoom(r, ourWorld);

        ter.renderFrame(ourWorld);
    }

    private static void addRoom(Room room, TETile[][] world) {
        for (Position p : room.getWallCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.WALL;
        }
        OurWorld.distinctRooms.add(room);
        for (Position p : room.getFloorCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.FLOOR;
        }
    }
}

//tasks: create rooms, create hallways,
// learn how to join then, add randomness
// deal with overlapping
//interact w input string