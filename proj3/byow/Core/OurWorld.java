package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class OurWorld {
    //Maximum dimensions for our world.
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    //Maximum width and height for the BIG rooms.
    private static final int maxWidth = WIDTH / 2;
    private static final int maxHeight = HEIGHT / 2;
    //Maximum width and height for SMALL rooms
    private static final int maxSWidth = WIDTH / 4;
    private static final int maxSHeight = HEIGHT / 4;
    //Maximum width and height for TINY rooms
    private static final int maxTWidth = WIDTH / 8;
    private static final int maxTHeight = HEIGHT / 8;
    //The maximum limit of which x and y values the bottom left corner of the room can go in order to prevent the problem of out of bounds.
    private static final int largestX = WIDTH - 3;
    private static final int largestY = HEIGHT - 3;
    public static Set<Position> coveredPositions = new HashSet<>();
    public static Set<Room> distinctRooms = new HashSet<>();
    public static ArrayHeapMinPQ<Room> listOfRooms = new ArrayHeapMinPQ<>();
    public static ArrayHeapMinPQ<Room> isolatedRooms = new ArrayHeapMinPQ<>();
    public static List<Position> openCoordinates = new LinkedList<>();

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

        int i = 10000;
        Random rand = new Random(1);
        generateRooms(i, rand, ourWorld);

        addOpenings(listOfRooms, ourWorld);
        generateHallways(ourWorld);
        
        ter.renderFrame(ourWorld);
    }

    private static void generateRooms(int numTrials, Random rand, TETile[][] world) {
        while (numTrials > 0) {
            Room r = new Room(new Position(rand.nextInt(largestX+1), rand.nextInt(largestY+1)),
                    rand.nextInt(maxWidth+1) + 4, rand.nextInt(maxHeight+1) + 4, rand);
            addRoom(r, world);
            Room sr = new Room(new Position(rand.nextInt(largestX+1), rand.nextInt(largestY+1)),
                    rand.nextInt(maxSWidth+1) + 4, rand.nextInt(maxSHeight+1) + 4, rand);
            addRoom(sr, world);
            Room tr = new Room(new Position(rand.nextInt(largestX+1), rand.nextInt(largestY+1)),
                    rand.nextInt(maxTWidth+1) + 4, rand.nextInt(maxTHeight+1) + 4, rand);
            addRoom(tr, world);
            numTrials--;
        }
    }

    private static void addRoom(Room room, TETile[][] world) {
        for (Position p : room.getWallCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.WALL;
        }
        if (!room.getOverlap()) {
            distinctRooms.add(room);
            listOfRooms.add(room, room.getBottomLeft().getX());
            isolatedRooms.add(room, room.getBottomLeft().getX());
        }
        for (Position p : room.getFloorCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.FLOOR;
        }
    }

    private static void addOpenings(ArrayHeapMinPQ<Room> listOfRooms, TETile[][] world) {
        while (listOfRooms.size() != 0) {
            Room room = listOfRooms.removeSmallest();
            for (Position p : room.getOpenCoordinates()) {
                world[p.getX()][p.getY()] = Tileset.FLOOR;
                openCoordinates.add(p);
            }
        }
    }

    private static void generateHallways(TETile[][] world) {
        while (openCoordinates.size() > 1) {
            AStarGraph<Position> pathway = new PathGraph();
            Position first = openCoordinates.remove(0);
            Position second = openCoordinates.remove(0);
            AStarSolver<Position> pathFinder = new AStarSolver<>(pathway, first, second, 30);
            generatePaths(pathFinder, world);
        }
        if (openCoordinates.size() == 1) {
            Position k = openCoordinates.remove(0);
            world[k.getX()][k.getY()] = Tileset.WALL;
        }
    }

    private static void generatePaths(AStarSolver<Position> path, TETile[][] world) {
        List<Position> chosenPath = path.solution();
        for (int i = 0; i < chosenPath.size() - 1; i++) {
            generatePathsHelper(chosenPath.get(i), chosenPath.get(i + 1), world);
        }
    }

    private static void generatePathsHelper(Position first, Position second, TETile[][] world) {
        if (first.getX() == second.getX()) {
            int bookmarkY = first.getY();
            int targetY = second.getY();

            if (bookmarkY > targetY) {
                while (bookmarkY > targetY) {
                    world[first.getX()][bookmarkY] = Tileset.FLOOR;
                    bookmarkY--;
                }

            } else if (bookmarkY < targetY) {
                while (bookmarkY < targetY) {
                    world[first.getX()][bookmarkY] = Tileset.FLOOR;
                    bookmarkY++;
                }
            }


        } else if (first.getY() == second.getY()) {
            int bookmarkX = first.getX();
            int targetX = second.getX();

            if (bookmarkX > targetX) {
                while (bookmarkX > targetX) {
                    world[bookmarkX][first.getY()] = Tileset.FLOOR;
                    bookmarkX--;
                }

            } else if (bookmarkX < targetX) {
                while (bookmarkX < targetX) {
                    world[bookmarkX][first.getY()] = Tileset.FLOOR;
                    bookmarkX++;
                }
            }

        }


    }
}

