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
    //public static Set<Position> coveredFloorPositions = new HashSet<>();
    public static List<Position> coveredWallPositions = new LinkedList<>();
    public static Set<Position> coveredPositions = new HashSet<>();
    public static Set<Room> distinctRooms = new HashSet<>();
    public static LinkedList<Room> listOfRooms = new LinkedList<>();
    public static LinkedList<Room> isolatedRooms = new LinkedList<>();
    public static List<Position> openCoordinates = new LinkedList<>();
    public static UnionFind roomsToConnect;
    //public static Map<Room, Integer> roomToNumber = new HashMap<>();
    public static Map<Integer, Room> numberToRoom = new HashMap<>();
    public static Map<Position, Room> openToRoom = new HashMap<>();
    public static Map<Position, Room> wallToRoom = new HashMap<>();
    // Created to account for only the open coordinates when addOpenings have been called.
    public static List<Position> initialOpenCoordinates = new LinkedList<>();

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
        generateRooms(i, rand, ourWorld);
        int k = isolatedRooms.size();
        roomsToConnect = new UnionFind(k);

        addOpenings(listOfRooms, ourWorld, rand);
        generateHallways(ourWorld, rand);
        connectRooms(rand, ourWorld);


        ter.renderFrame(ourWorld);
    }



    public static void generateRooms(int numTrials, Random rand, TETile[][] world) {
        //Keeps track of how many rooms there are
        int i = 0;
        while (numTrials > 0) {
            Room r = new Room(new Position(rand.nextInt(largestX+1), rand.nextInt(largestY+1)),
                    rand.nextInt(maxWidth+1) + 4, rand.nextInt(maxHeight+1) + 4, i);
            addRoom(r, world);
            if (r.getOverlap()) {
                numTrials--;
                continue;
            }
            numberToRoom.put(i, r);
            i++;
            numTrials--;
        }
    }

    public static void addRoom(Room room, TETile[][] world) {
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

    public static void addOpenings(LinkedList<Room> listOfRooms, TETile[][] world, Random random) {
        while (listOfRooms.size() != 0) {
            Room room = listOfRooms.remove();
            for (Position p : room.getOpenCoordinates(random)) {
                world[p.getX()][p.getY()] = Tileset.FLOOR;
                initialOpenCoordinates.add(p);
                openCoordinates.add(p);
                openToRoom.put(p, room);
                wallToRoom.remove(p);
                coveredWallPositions.remove(p);
            }
        }
    }

//problem with generating hallways is that it creates additional closed rooms.
    //Use the disjoint set data structure to ensure connectedness
    public static void generateHallways(TETile[][] world, Random random) {
        int i = initialOpenCoordinates.size();
        while (i > 1) {
            AStarGraph<Position> pathway = new PathGraph();
            int index1 = random.nextInt(i);
            int index2 = random.nextInt(i);
            Position first = initialOpenCoordinates.get(index1);
            Position second = initialOpenCoordinates.get(index2);
            Room rFirst = openToRoom.get(first);
            Room rSecond = openToRoom.get(second);


           //Ensures that different rooms are connected
            while (rFirst.equals(rSecond) && i > 4) {
                index2 = random.nextInt(i);
                second = initialOpenCoordinates.get(index2);
                rSecond = openToRoom.get(second);
            }
            boolean allSameRoom = false;
            if (i <= 4) {
                int k = 0;
                for (; k < initialOpenCoordinates.size(); k++) {
                    if (!rFirst.equals(openToRoom.get(initialOpenCoordinates.get(k)))) {
                        index2 = k;
                        second = initialOpenCoordinates.get(k);
                        break;
                    }
                }
                if (k == initialOpenCoordinates.size()) {
                    allSameRoom = true;
                }
                if (allSameRoom) {
                    int size = initialOpenCoordinates.size();
                    //Undoing the action from addOpenings
                    for (int count = 0; count < size; count++) {
                        i--;
                        Position p = initialOpenCoordinates.remove(0);
                        world[p.getX()][p.getY()] = Tileset.WALL;
                        openCoordinates.remove(p);
                        openToRoom.remove(p);
                        wallToRoom.put(p, rFirst);
                        coveredWallPositions.add(p);
                    }
                }
            }

            if (!allSameRoom) {
                i = i - 2;
                initialOpenCoordinates.remove(index1);
                if (index2 > index1) {
                    index2--;
                }
                initialOpenCoordinates.remove(index2);
                // For the world to recognize that these two rooms are connected through a disjoint set.
                int v1 = openToRoom.get(first).getId();
                int v2 = openToRoom.get(second).getId();
                roomsToConnect.connect(v1, v2);

                AStarSolver<Position> pathFinder = new AStarSolver<>(pathway, first, second, 30);
                generatePaths(pathFinder, world);
            }
        }
        if (i == 1) {
            Position k = initialOpenCoordinates.remove(0);
            world[k.getX()][k.getY()] = Tileset.WALL;
        }
    }
/**
    //Checks to see if there is one root, by checking if the parent of that root is negative, then tells if the world is connected if the size of that root is equal to the total number of rooms.
    private static boolean isWorldConnected() {
        for (int i = 0; i < roomsToConnect.parent.length; i++) {
            if (roomsToConnect.parent(i) < 0) {
                if (roomsToConnect.sizeOf(i) == roomsToConnect.parent.length) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }*/

/**
    public static boolean sharedCoordinates(Room first, Room second) {
        return first.equals(second);
    }*/

    private static void generatePaths(AStarSolver<Position> path, TETile[][] world) {
        List<Position> chosenPath = path.solution();
        for (int i = 0; i < chosenPath.size() - 1; i++) {
            generatePathsHelper(chosenPath.get(i), chosenPath.get(i + 1), world);
        }
    }


    private static boolean roomsConnected(Room first, Room second) {
        return roomsToConnect.isConnected(first.getId(), second.getId());
    }

    private static void deleteWallCoordinates(Position position) {
        coveredWallPositions.remove(position);
        wallToRoom.remove(position);
    }

    //Reconsider if you really need to add addOpenCoordinates method.
    private static void generatePathsHelper(Position first, Position second, TETile[][] world) {
        if (first.getX() == second.getX()) {
            int bookmarkY = first.getY();
            int targetY = second.getY();

            if (bookmarkY > targetY) {
                checkSurroundings(first.getX(), bookmarkY, world);
                while (bookmarkY > targetY) {
                    world[first.getX()][bookmarkY] = Tileset.FLOOR;
                    if (coveredWallPositions.contains(new Position(first.getX(), bookmarkY))) {
                        deleteWallCoordinates(new Position(first.getX(), bookmarkY));
                    }
                    //Add open coordinates.
                    coveredPositions.add(new Position(first.getX(), bookmarkY));
                    crossRoomCheck(first.getX(), bookmarkY);
                    checkSurroundingsX(first.getX(), bookmarkY, world);
                    bookmarkY--;
                }
                checkSurroundings(first.getX(), bookmarkY, world);
            } else if (bookmarkY < targetY) {
                checkSurroundings(first.getX(), bookmarkY, world);
                while (bookmarkY < targetY) {
                    world[first.getX()][bookmarkY] = Tileset.FLOOR;
                    coveredPositions.add(new Position(first.getX(), bookmarkY));
                    crossRoomCheck(first.getX(), bookmarkY);
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
                    coveredPositions.add(new Position(bookmarkX, first.getY()));
                    crossRoomCheck(bookmarkX, first.getY());
                    checkSurroundingsY(bookmarkX, first.getY(), world);
                    bookmarkX--;
                }
                checkSurroundings(bookmarkX, first.getY(), world);
            } else if (bookmarkX < targetX) {
                checkSurroundings(bookmarkX, first.getY(), world);
                while (bookmarkX < targetX) {
                    world[bookmarkX][first.getY()] = Tileset.FLOOR;
                    coveredPositions.add(new Position(bookmarkX, first.getY()));
                    crossRoomCheck(bookmarkX, first.getY());
                    checkSurroundingsY(bookmarkX, first.getY(), world);
                    bookmarkX++;
                }
                checkSurroundings(bookmarkX, first.getY(), world);
            }

        }


    }

    //Checks if the path crosses the border of a room. If so, add the open coordinate.
    private static void crossRoomCheck(int x, int y) {
        Position p = new Position(x, y);
        for (Room r : isolatedRooms) {
            if (r.getWalls().contains(p) && !openCoordinates.contains(p)) {
                openCoordinates.add(p);
            }
        }
    }



//Can we assume that if there is an open coordinate in a room r, the room is connected?
    //For each room that has a negative parent, connect that room with a room that has a positive parent. Assumes that world is not yet connected.
    //Once called, does this method guarantee that all the rooms will be connected?
    public static void connectRooms(Random random, TETile[][] world) {
        Map<Integer, Integer> verticesToConnect = new HashMap<>();
        for (int i = 0; i < roomsToConnect.parent.length; i++) {


            if (roomsToConnect.parent[i] < 0 && Math.abs(roomsToConnect.parent(i)) != roomsToConnect.parent.length) {
                Room toConnect = numberToRoom.get(i);
                Position start = toConnect.openHole(random);
                wallToRoom.remove(start);
                coveredWallPositions.remove(start);
                //openToRoom.put(start, toConnect);

                //Getting a random wall coordinate of a random room.
                int index = random.nextInt(coveredWallPositions.size());
                Position target = coveredWallPositions.get(index);
                Room targetRoom = wallToRoom.get(target);
                //To ensure connectedness, get the parent room of that room.
                Room parentRoom = numberToRoom.get(roomsToConnect.find(targetRoom.getId()));

                //Get a random wall coordinate from the parent room.
                int indexParent = random.nextInt(parentRoom.getWalls().size());
                //randomWall could be an open coordinate of the parent room
                Position randomWall = parentRoom.getWalls().get(indexParent);
                //check that it is not an open coordinate of the parent room. If it is an open coordinate, then keep picking until you found a wall coordinate
                while (openCoordinates.contains(randomWall)) {
                    indexParent = random.nextInt(parentRoom.getWalls().size());
                    randomWall = parentRoom.getWalls().get(indexParent);
                }


                //openToRoom.put(randomWall, parentRoom);
                while (toConnect.equals(parentRoom)) {

                    //openToRoom.remove(randomWall);

                    index = random.nextInt(coveredWallPositions.size());
                    target = coveredWallPositions.get(index);
                    targetRoom = wallToRoom.get(target);
                    parentRoom = numberToRoom.get(roomsToConnect.find(targetRoom.getId()));
                    indexParent = random.nextInt(parentRoom.getWalls().size());
                    randomWall = parentRoom.getWalls().get(indexParent);
                    while (openCoordinates.contains(randomWall)) {
                        indexParent = random.nextInt(parentRoom.getWalls().size());
                        randomWall = parentRoom.getWalls().get(indexParent);
                    }

                    //openToRoom.put(randomWall, parentRoom);
                }

                //Delete the wall coordinate
                wallToRoom.remove(randomWall);
                coveredWallPositions.remove(randomWall);
                world[randomWall.getX()][randomWall.getY()] = Tileset.FLOOR;

                //Add the change to the disjoint set later
                verticesToConnect.put(toConnect.getId(), parentRoom.getId());

                //Generate the path
                PathGraph newPath = new PathGraph();
                generatePaths(new AStarSolver<>(newPath, start, randomWall, 30), world);
            }
        }
        for (Integer i : verticesToConnect.keySet()) {
            roomsToConnect.connect(i, verticesToConnect.get(i));
        }
    }


    private static void checkSurroundingsX(int x, int y, TETile[][] world) {
        if (!Room.overlap(x - 1, y, coveredPositions)) {
            if (x - 1 >= 0) {
                world[x - 1][y] = Tileset.WALL;
                coveredPositions.add(new Position(x - 1, y));
            } else {
                world[x][y] = Tileset.WALL;
            }
        }

        if (!Room.overlap(x + 1, y, coveredPositions)) {
            if (x + 1 < getXDimension()) {
                world[x + 1][y] = Tileset.WALL;
                coveredPositions.add(new Position(x + 1, y));
            } else {
                world[x][y] = Tileset.WALL;
            }
        }
    }

    private static void checkSurroundingsY(int x, int y, TETile[][] world) {
        if (!Room.overlap(x, y + 1, coveredPositions)) {
            if (y + 1 < getYDimension()) {
                world[x][y + 1] = Tileset.WALL;
                coveredPositions.add(new Position(x, y + 1));
            } else {
                world[x][y] = Tileset.WALL;
            }
        }
        if (!Room.overlap(x, y - 1, coveredPositions)) {
            if (y - 1 >= 0) {
                world[x][y - 1] = Tileset.WALL;
                coveredPositions.add(new Position(x, y - 1));
            } else {
                world[x][y] = Tileset.WALL;
            }
        }
    }

    private static void checkSurroundings(int x, int y, TETile[][] world) {
        //Top left
        if (!Room.overlap(x - 1, y + 1, coveredPositions)) {
            //Check for out of bounds
            if (x - 1 >= 0 && y + 1 < getYDimension()) {
                world[x - 1][y + 1] = Tileset.WALL;
                coveredPositions.add(new Position(x - 1, y + 1));
            }
        }

        //Top right
        if (!Room.overlap(x + 1, y + 1, coveredPositions)) {
            if (x + 1 < getXDimension() && y + 1 < getYDimension()) {
                world[x + 1][y + 1] = Tileset.WALL;
                coveredPositions.add(new Position(x + 1, y + 1));
            }
        }

        //bottom left
        if (!Room.overlap(x - 1, y - 1, coveredPositions)) {
            if (x - 1 >= 0 && y - 1 >= 0) {
                world[x - 1][y - 1] = Tileset.WALL;
                coveredPositions.add(new Position(x - 1, y - 1));
            }
        }

        //bottom right
        if (!Room.overlap(x + 1, y - 1, coveredPositions)) {
            if (x + 1 < getXDimension() && y - 1 >= 0) {
                world[x + 1][y - 1] = Tileset.WALL;
                coveredPositions.add(new Position(x + 1, y - 1));
            }
        }
        //Top
        if (!Room.overlap(x, y + 1, coveredPositions)) {
            if (y + 1 < getYDimension()) {
                world[x][y + 1] = Tileset.WALL;
                coveredPositions.add(new Position(x, y + 1));
            } else {
                world[x][y] = Tileset.WALL;
            }
        }
        //Bottom
        if (!Room.overlap(x, y - 1, coveredPositions)) {
            if (y - 1 >= 0) {
                world[x][y - 1] = Tileset.WALL;
                coveredPositions.add(new Position(x, y - 1));
            } else {
                world[x][y] = Tileset.WALL;
            }
        }
        //Left
        if (!Room.overlap(x - 1, y, coveredPositions)) {
            if (x - 1 >= 0) {
                world[x - 1][y] = Tileset.WALL;
                coveredPositions.add(new Position(x - 1, y));
            } else {
                world[x][y] = Tileset.WALL;
            }
        }
        //Right
        if (!Room.overlap(x + 1, y, coveredPositions)) {
            if (x + 1 < getXDimension()) {
                world[x + 1][y] = Tileset.WALL;
                coveredPositions.add(new Position(x + 1, y));
            } else {
                world[x][y] = Tileset.WALL;
            }
        }

    }
}

