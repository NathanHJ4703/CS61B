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
    public static Set<Position> coveredFloorPositions = new HashSet<>();
    public static Set<Position> coveredWallPositions = new HashSet<>();
    public static Set<Position> coveredPositions = new HashSet<>();
    public static Set<Room> distinctRooms = new HashSet<>();
    public static LinkedList<Room> listOfRooms = new LinkedList<>();
    public static LinkedList<Room> isolatedRooms = new LinkedList<>();
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
            listOfRooms.add(room);
            isolatedRooms.add(room);
        }
        for (Position p : room.getFloorCoordinates()) {
            world[p.getX()][p.getY()] = Tileset.FLOOR;
        }
    }

    private static void addOpenings(LinkedList<Room> listOfRooms, TETile[][] world) {
        while (listOfRooms.size() != 0) {
            Room room = listOfRooms.remove();
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
                checkSurroundings(first.getX(), bookmarkY, world);
                while (bookmarkY > targetY) {
                    world[first.getX()][bookmarkY] = Tileset.FLOOR;
                    checkSurroundingsX(first.getX(), bookmarkY, world);
                    bookmarkY--;
                }
                checkSurroundings(first.getX(), bookmarkY, world);
            } else if (bookmarkY < targetY) {
                checkSurroundings(first.getX(), bookmarkY, world);
                while (bookmarkY < targetY) {
                    world[first.getX()][bookmarkY] = Tileset.FLOOR;
                    checkSurroundingsX(first.getX(), bookmarkY, world);
                    bookmarkY++;
                }
                checkSurroundings(first.getX(), bookmarkY, world);
            }


        } else if (first.getY() == second.getY()) {
            int bookmarkX = first.getX();
            int targetX = second.getX();

            if (bookmarkX > targetX) {
                checkSurroundings(bookmarkX, first.getY(), world);
                while (bookmarkX > targetX) {
                    world[bookmarkX][first.getY()] = Tileset.FLOOR;
                    checkSurroundingsY(bookmarkX, first.getY(), world);
                    bookmarkX--;
                }
                checkSurroundings(bookmarkX, first.getY(), world);
            } else if (bookmarkX < targetX) {
                checkSurroundings(bookmarkX, first.getY(), world);
                while (bookmarkX < targetX) {
                    world[bookmarkX][first.getY()] = Tileset.FLOOR;
                    checkSurroundingsY(bookmarkX, first.getY(), world);
                    bookmarkX++;
                }
                checkSurroundings(bookmarkX, first.getY(), world);
            }

        }


    }

    private static void checkSurroundingsX(int x, int y, TETile[][] world) {
        if (!Room.overlap(x - 1, y, coveredPositions)) {
            world[x - 1][y] = Tileset.WALL;
            coveredPositions.add(new Position(x - 1, y));
        }
        if (!Room.overlap(x + 1, y, coveredPositions)) {
            world[x + 1][y] = Tileset.WALL;
            coveredPositions.add(new Position(x + 1, y));
        }
    }

    private static void checkSurroundingsY(int x, int y, TETile[][] world) {
        if (!Room.overlap(x, y + 1, coveredPositions)) {
            world[x][y + 1] = Tileset.WALL;
            coveredPositions.add(new Position(x, y + 1));
        }
        if (!Room.overlap(x, y - 1, coveredPositions)) {
            world[x][y - 1] = Tileset.WALL;
            coveredPositions.add(new Position(x, y - 1));
        }
    }

    private static void checkSurroundings(int x, int y, TETile[][] world) {
        //Top left
        if (!Room.overlap(x - 1, y + 1, coveredPositions)) {
            world[x - 1][y + 1] = Tileset.WALL;
            coveredPositions.add(new Position(x - 1, y + 1));
        }

        //Top right
        if (!Room.overlap(x + 1, y + 1, coveredPositions)) {
            world[x + 1][y + 1] = Tileset.WALL;
            coveredPositions.add(new Position(x + 1, y + 1));
        }

        //bottom left
        if (!Room.overlap(x - 1, y - 1, coveredPositions)) {
            world[x - 1][y - 1] = Tileset.WALL;
            coveredPositions.add(new Position(x - 1, y - 1));
        }

        //bottom right
        if (!Room.overlap(x + 1, y - 1, coveredPositions)) {
            world[x + 1][y - 1] = Tileset.WALL;
            coveredPositions.add(new Position(x + 1, y - 1));
        }
        if (!Room.overlap(x, y + 1, coveredPositions)) {
            world[x][y + 1] = Tileset.WALL;
            coveredPositions.add(new Position(x, y + 1));
        }
        if (!Room.overlap(x, y - 1, coveredPositions)) {
            world[x][y - 1] = Tileset.WALL;
            coveredPositions.add(new Position(x, y - 1));
        }
        if (!Room.overlap(x - 1, y, coveredPositions)) {
            world[x - 1][y] = Tileset.WALL;
            coveredPositions.add(new Position(x - 1, y));
        }
        if (!Room.overlap(x + 1, y, coveredPositions)) {
            world[x + 1][y] = Tileset.WALL;
            coveredPositions.add(new Position(x + 1, y));
        }

    }
}

