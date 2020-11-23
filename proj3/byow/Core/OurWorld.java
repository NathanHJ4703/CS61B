package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

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
    public static ArrayHeapMinPQ<Room> listOfRooms = new ArrayHeapMinPQ<>();

    public static int getYDimension() {
        return HEIGHT;
    }

    public static int getXDimension() {
        return WIDTH;
    }

    public static int getMaxWidth() {
        return maxWidth;
    }

    public static int getMaxHeight() {
        return maxHeight;
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
        Random rand = new Random(1);
        while(i > 0) {
            Room r = new Room(new Position(rand.nextInt(largestX+1), rand.nextInt(largestY+1)),
                    rand.nextInt(maxWidth+1) + 4, rand.nextInt(maxHeight+1) + 4, rand);
            addRoom(r, ourWorld);
            i--;
        }
        //Room r1 = listOfRooms.removeSmallest();
        //Room r2 = listOfRooms.getSmallest();
        //addHallway(Room.connect(r1, r2), ourWorld);
        ter.renderFrame(ourWorld);
    }

    private static void addRoom(Room room, TETile[][] world) {
        for (Position p : room.getWallCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.WALL;
        }
        if (!room.getOverlap()) {
            OurWorld.distinctRooms.add(room);
            OurWorld.listOfRooms.add(room, room.getBottomLeft().getX());
        }
        for (Position p : room.getFloorCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.FLOOR;
        }
    }
/**
    private static void addHallway(Room hallway, TETile[][] world) {
        for (Position p : hallway.getWallCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.WALL;
        }
        for (Position p : hallway.getFloorCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.FLOOR;
        }
    }*/
}

//tasks: create rooms, create hallways,
// learn how to join then, add randomness
// deal with overlapping
//interact w input string