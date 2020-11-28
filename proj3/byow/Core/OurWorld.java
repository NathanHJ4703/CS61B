package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;


/** The world that we generate for rooms and hallways.
 * @author nathanpak
 */
public class OurWorld {
    /** Maximum width for our world. */
    private static final int WIDTH = 60;
    /** Maximum height for our world. */
    private static final int HEIGHT = 30;
    /** Maximum width for room. */
    private static final int MAXWIDTH = WIDTH / 2;
    /** Maximum height for room. */
    private static final int MAXHEIGHT = HEIGHT / 2;

    /** The maximum limit of which x and y values the bottom left
     * corner of the room
     * can go in order to prevent the problem of out of bounds. */
    private static final int LARGESTX = WIDTH - 3;
    /** The maximum limit of which y values from the bottom left
     * corner of the room can go in order to prevent the problem
     * of out of bounds. */
    private static final int LARGESTY = HEIGHT - 3;

    /** Gets the YDimension.
     *
     * @return The height of window.
     */
    public static int getYDimension() {
        return HEIGHT;
    }

    /** XDimension size.
     *
     * @return The width of window
     */
    public static int getXDimension() {
        return WIDTH;
    }

    /** Max width of a room.
     *
     * @return The max width of a room
     */
    public static int getMaxWidth() {
        return MAXWIDTH;
    }

    /** The max height of a room.
     *
     * @return The max height of a room.
     */
    public static int getMaxHeight() {
        return MAXHEIGHT;
    }

    /** Creates a generated world with hallways and rooms connected.
     * @param args No argument */
    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);


        TETile[][] ourWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                ourWorld[x][y] = Tileset.NOTHING;
            }
        }

        RoomTracker rTracker = new RoomTracker();
        PositionTracker pTracker = new PositionTracker();
        OpenCoordTracker oTracker = new OpenCoordTracker();

        int i = 10;
        Random rand = new Random(1);
        generateRooms(i, rand, ourWorld, rTracker, pTracker);
        int k = rTracker.getIsolatedRooms().size();
        rTracker.constructUnionFind(k);

        addOpenings(rTracker.getListOfRooms(), ourWorld, rand, oTracker,
                pTracker);
        generateHallways(ourWorld, rand, oTracker, pTracker,
                rTracker);
        connectRooms(rand, ourWorld, rTracker, pTracker,
                oTracker);


        ter.renderFrame(ourWorld);
    }


    /** Generates random rooms for numTrials amount of times. NumTrials does not
     * correspond to the number of rooms.
     *
     * @param numTrials How many times it should repeat
     * @param rand The random object
     * @param world The window.
     * @param rTracker Tracker for the rooms
     * @param pTracker Tracker for the positions.
     */
    public static void generateRooms(int numTrials, Random rand,
                                     TETile[][] world,
                                     RoomTracker rTracker,
                                     PositionTracker pTracker) {

        int i = 0;
        while (numTrials > 0) {
            Room r = new Room(new Position(rand.nextInt(LARGESTX + 1),
                    rand.nextInt(LARGESTY + 1)),
                    rand.nextInt(MAXWIDTH + 1) + 4,
                    rand.nextInt(MAXHEIGHT + 1) + 4, i);
            addRoom(r, world, rTracker, pTracker);
            if (r.getOverlap()) {
                numTrials--;
                continue;
            }
            rTracker.giveID(i, r);
            i++;
            numTrials--;
        }
    }

    /** Adds the room.
     *
     * @param room Given room
     * @param world The window
     * @param rTracker Tracker for rooms
     * @param pTracker Tracker for positions
     */
    public static void addRoom(Room room, TETile[][] world,
                               RoomTracker rTracker,
                               PositionTracker pTracker) {
        for (Position p : room.getWallCoordinates(pTracker)) {
            world[p.getX()][p.getY()] = Tileset.WALL;
        }
        if (!room.getOverlap()) {
            rTracker.addRoom(room);
        }
        for (Position p : room.getFloorCoordinates(pTracker, rTracker)) {
            world[p.getX()][p.getY()] = Tileset.FLOOR;
        }
    }

    /** Adds random openings for each room.
     *
     * @param listOfRooms List of rooms
     * @param world Window
     * @param random Random object
     * @param oTracker Tracker for open coordinates of the rooms
     * @param pTracker Tracker for positions
     */
    public static void addOpenings(LinkedList<Room> listOfRooms,
                                   TETile[][] world,
                                   Random random, OpenCoordTracker oTracker,
                                   PositionTracker pTracker) {
        while (listOfRooms.size() != 0) {
            Room room = listOfRooms.remove();
            for (Position p : room.getOpenCoordinates(random)) {
                world[p.getX()][p.getY()] = Tileset.FLOOR;
                oTracker.addInitialOpenCoordinates(p);
                oTracker.addOpenCoordinates(p);
                oTracker.addOpenToRoom(p, room);
                pTracker.removeWallToRoom(p);
                pTracker.removeCoveredWallPositions(p);
            }
        }
    }

    /** Generates hallways by connecting one open coordinate to another
     * with a hallway for each room.
     *
     * @param world Window
     * @param random Random object
     * @param openCoordTracker Tracker for open coordinates
     * @param positionTracker Tracker for positions
     * @param roomTracker Tracker for rooms
     */
    public static void generateHallways(TETile[][] world, Random random,
                                        OpenCoordTracker openCoordTracker,
                                        PositionTracker positionTracker,
                                        RoomTracker roomTracker) {
        int i = openCoordTracker.getInitialOpenCoordinates().size();
        while (i > 1) {
            AStarGraph<Position> pathway = new PathGraph();
            int index1 = random.nextInt(i);
            int index2 = random.nextInt(i);
            Position first =
                    openCoordTracker.getInitialOpenCoordinates().get(index1);
            Position second =
                    openCoordTracker.getInitialOpenCoordinates().get(index2);
            Room rFirst = openCoordTracker.getOpenToRoom().get(first);
            Room rSecond = openCoordTracker.getOpenToRoom().get(second);
            while (rFirst.equals(rSecond) && i > 4) {
                index2 = random.nextInt(i);
                second =
                        openCoordTracker.getInitialOpenCoordinates().
                                get(index2);
                rSecond = openCoordTracker.getOpenToRoom().
                        get(second);
            }
            boolean allSameRoom = false;
            if (i <= 4) {
                int k = 0;
                for (; k
                        < openCoordTracker.getInitialOpenCoordinates().size(); k++) {
                    if (!rFirst.equals(
                            openCoordTracker.getOpenToRoom().get(
                                    openCoordTracker.
                                            getInitialOpenCoordinates().get(
                                            k)))) {
                        index2 = k;
                        second = openCoordTracker.getInitialOpenCoordinates().get(k);
                        break;
                    }
                }
                if (k == openCoordTracker.getInitialOpenCoordinates().size()) {
                    allSameRoom = true;
                }
                if (allSameRoom) {
                    i = wallsForFour(openCoordTracker, positionTracker, rFirst, i, world);
                }

            }
            if (!allSameRoom) {
                i = i - 2;
                openCoordTracker.removeInitialOpenCoordinates(index1);
                if (index2 > index1) {
                    index2--;
                }
                openCoordTracker.removeInitialOpenCoordinates(index2);

                int v1 = openCoordTracker.getOpenToRoom().get(first).getId();
                int v2 = openCoordTracker.getOpenToRoom().get(second).getId();
                roomTracker.connectRooms(v1, v2);

                AStarSolver<Position> pathFinder = new AStarSolver<>(pathway, first, second, 30);
                generatePaths(pathFinder, world, positionTracker, roomTracker, openCoordTracker);
            }
        }
        if (i == 1) {
            Position k = openCoordTracker.removeInitialOpenCoordinates(0);
            world[k.getX()][k.getY()] = Tileset.WALL;
        }
    }

    /** Converts open coordinates back into walls.
     *
     * @param openCoordTracker Tracker for open coordinates
     * @param positionTracker Tracker for positions
     * @param rFirst First room
     * @param i amount of open coordinates left
     * @param world window
     * @return The amount of open coordinates left after placing walls
     */
    private static int wallsForFour(OpenCoordTracker
                                            openCoordTracker,
                                    PositionTracker positionTracker,
                                               Room rFirst, int i, TETile[][] world) {

        int size = openCoordTracker.getInitialOpenCoordinates().size();
        for (int count = 0; count < size; count++) {
            i--;
            Position p = openCoordTracker.removeInitialOpenCoordinates(0);
            world[p.getX()][p.getY()] = Tileset.WALL;
            openCoordTracker.removeOpenCoordinates(p);
            openCoordTracker.removeOpenToRoom(p);
            positionTracker.addWallToRoom(p, rFirst);
            positionTracker.addCoveredWallPositions(p);
        }

        return i;
    }

    /** Generates the path from one vertex to another.
     *
     * @param path The path constructed by A*Solver
     * @param world The window
     * @param positionTracker Tracker for positions
     * @param roomTracker Tracker for rooms
     * @param openCoordTracker Tracker for open coordinates
     */
    private static void generatePaths(AStarSolver<Position> path, TETile[][] world,
                                      PositionTracker positionTracker,
                                      RoomTracker roomTracker, OpenCoordTracker openCoordTracker) {
        List<Position> chosenPath = path.solution();
        for (int i = 0; i < chosenPath.size() - 1; i++) {
            generatePathsHelper(chosenPath.get(i), chosenPath.get(i + 1),
                    world, positionTracker, roomTracker, openCoordTracker);
        }
    }

    /** Deletes wall coordinates.
     *
     * @param position Given position
     * @param positionTracker Tracker for positions
     */
    private static void deleteWallCoordinates(Position position, PositionTracker positionTracker) {
        positionTracker.removeCoveredWallPositions(position);
        positionTracker.removeWallToRoom(position);
    }

    /** Builds a path from one vertex to another and checks the surrounding in every
     *  tile placed while going on the path.
     *
     * @param first Start position
     * @param second End position
     * @param world Window
     * @param positionTracker Tracker for positions
     * @param roomTracker Tracker for rooms
     * @param openCoordTracker Tracker for open coordinates
     */
    private static void generatePathsHelper(Position first, Position second, TETile[][] world,
                                            PositionTracker positionTracker,
                                            RoomTracker roomTracker,
                                            OpenCoordTracker openCoordTracker) {
        if (first.getX() == second.getX()) {
            int bookmarkY = first.getY();
            int targetY = second.getY();

            if (bookmarkY > targetY) {
                checkSurroundings(first.getX(), bookmarkY, world, positionTracker);
                while (bookmarkY > targetY) {
                    world[first.getX()][bookmarkY] = Tileset.FLOOR;
                    if (positionTracker.getCoveredWallPositions().contains(
                            new Position(first.getX(), bookmarkY))) {
                        deleteWallCoordinates(new Position(first.getX(),
                                bookmarkY), positionTracker);
                    }

                    positionTracker.addCoveredPositions(new Position(first.getX(), bookmarkY));
                    crossRoomCheck(first.getX(), bookmarkY, roomTracker, openCoordTracker);
                    checkSurroundingsX(first.getX(), bookmarkY, world, positionTracker);
                    bookmarkY--;
                }
                checkSurroundings(first.getX(), bookmarkY, world, positionTracker);
            } else if (bookmarkY < targetY) {
                checkSurroundings(first.getX(), bookmarkY, world, positionTracker);
                while (bookmarkY < targetY) {
                    world[first.getX()][bookmarkY] = Tileset.FLOOR;
                    positionTracker.addCoveredPositions(new Position(first.getX(), bookmarkY));
                    crossRoomCheck(first.getX(), bookmarkY, roomTracker, openCoordTracker);
                    checkSurroundingsX(first.getX(), bookmarkY, world, positionTracker);
                    bookmarkY++;
                }
                checkSurroundings(first.getX(), bookmarkY, world, positionTracker);
            }


        } else if (first.getY() == second.getY()) {
            int bookmarkX = first.getX();
            int targetX = second.getX();

            if (bookmarkX > targetX) {
                checkSurroundings(bookmarkX, first.getY(), world, positionTracker);
                while (bookmarkX > targetX) {
                    world[bookmarkX][first.getY()] = Tileset.FLOOR;
                    positionTracker.addCoveredPositions(new Position(bookmarkX, first.getY()));
                    crossRoomCheck(bookmarkX, first.getY(), roomTracker, openCoordTracker);
                    checkSurroundingsY(bookmarkX, first.getY(), world, positionTracker);
                    bookmarkX--;
                }
                checkSurroundings(bookmarkX, first.getY(), world, positionTracker);
            } else if (bookmarkX < targetX) {
                checkSurroundings(bookmarkX, first.getY(), world, positionTracker);
                while (bookmarkX < targetX) {
                    world[bookmarkX][first.getY()] = Tileset.FLOOR;
                    positionTracker.addCoveredPositions(new Position(bookmarkX, first.getY()));
                    crossRoomCheck(bookmarkX, first.getY(), roomTracker, openCoordTracker);
                    checkSurroundingsY(bookmarkX, first.getY(), world, positionTracker);
                    bookmarkX++;
                }
                checkSurroundings(bookmarkX, first.getY(), world, positionTracker);
            }

        }


    }

    /** Checks if a path crosses the border of a room and adds the open coordinate
     * to the tracker if it does so.
     * @param x x coordinate
     * @param y y coordinate
     * @param roomTracker tracker for rooms
     * @param openCoordTracker tracker for open coordinates
     * */
    private static void crossRoomCheck(int x, int y,
                                       RoomTracker roomTracker,
                                       OpenCoordTracker openCoordTracker) {
        Position p = new Position(x, y);
        for (Room r : roomTracker.getIsolatedRooms()) {
            if (r.getWalls().contains(p) && !openCoordTracker.getOpenCoordinates().contains(p)) {
                openCoordTracker.addOpenCoordinates(p);
            }
        }
    }


    /** Connects the rooms together if the world hasn't been connected yet from calling
     * generateHallways method.
     *
     * @param random Random object
     * @param world Window
     * @param roomTracker Tracker for rooms
     * @param positionTracker Tracker for positions
     * @param openCoordTracker Tracker for open coordinates
     */
    public static void connectRooms(Random random, TETile[][] world, RoomTracker roomTracker,
                                    PositionTracker positionTracker,
                                    OpenCoordTracker openCoordTracker) {
        Map<Integer, Integer> verticesToConnect = new HashMap<>();
        UnionFind disjointSet = roomTracker.getRoomsToConnect();
        for (int i = 0; i < disjointSet.getParent().length; i++) {


            if (disjointSet.getParent()[i] < 0
                    && Math.abs(disjointSet.parent(i)) != disjointSet.getParent().length) {
                Room toConnect = roomTracker.getNumberToRoom().get(i);
                Position start = toConnect.openHole(random);
                positionTracker.removeWallToRoom(start);
                positionTracker.removeCoveredWallPositions(start);


                int index = random.nextInt(positionTracker.getCoveredWallPositions().size());
                Position target = positionTracker.getCoveredWallPositions().get(index);
                Room targetRoom = positionTracker.getWallToRoom().get(target);

                Room parentRoom = roomTracker.getNumberToRoom().get(
                        disjointSet.find(targetRoom.getId()));

                int indexParent = random.nextInt(parentRoom.getWalls().size());

                Position randomWall = parentRoom.getWalls().get(indexParent);

                while (openCoordTracker.getOpenCoordinates().contains(randomWall)) {
                    indexParent = random.nextInt(parentRoom.getWalls().size());
                    randomWall = parentRoom.getWalls().get(indexParent);
                }



                while (toConnect.equals(parentRoom)) {


                    index = random.nextInt(positionTracker.getCoveredWallPositions().size());
                    target = positionTracker.getCoveredWallPositions().get(index);
                    targetRoom = positionTracker.getWallToRoom().get(target);
                    parentRoom = roomTracker.getNumberToRoom().get(
                            disjointSet.find(targetRoom.getId()));
                    indexParent = random.nextInt(parentRoom.getWalls().size());
                    randomWall = parentRoom.getWalls().get(indexParent);
                    while (openCoordTracker.getOpenCoordinates().contains(randomWall)) {
                        indexParent = random.nextInt(parentRoom.getWalls().size());
                        randomWall = parentRoom.getWalls().get(indexParent);
                    }


                }


                positionTracker.removeWallToRoom(randomWall);
                positionTracker.removeCoveredWallPositions(randomWall);
                world[randomWall.getX()][randomWall.getY()] = Tileset.FLOOR;

                verticesToConnect.put(toConnect.getId(), parentRoom.getId());

                PathGraph newPath = new PathGraph();
                generatePaths(new AStarSolver<>(newPath, start, randomWall, 30), world,
                        positionTracker, roomTracker, openCoordTracker);
            }
        }
        for (Integer i : verticesToConnect.keySet()) {
            roomTracker.connectRooms(i, verticesToConnect.get(i));
        }
    }


    /** Checks the surroundings horizontally and places a wall if there is nothing there.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param world window
     * @param positionTracker tracker for positions
     */
    private static void checkSurroundingsX(int x, int y, TETile[][] world,
                                           PositionTracker positionTracker) {
        Set<Position> coveredPositions = positionTracker.getCoveredPositions();
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

    /** Checks the surroundings vertically and places a wall if there is nothing there.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param world window
     * @param positionTracker Tracker for positions
     */
    private static void checkSurroundingsY(int x, int y, TETile[][] world,
                                           PositionTracker positionTracker) {
        Set<Position> coveredPositions = positionTracker.getCoveredPositions();
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

    /** Checks the surroundings in all tiles surrounding the position,
     * including the tiles diagonal to it.
     * It places a wall if there is nothing there.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param world window
     * @param positionTracker Tracker for positions
     */
    private static void checkSurroundings(int x, int y, TETile[][] world,
                                          PositionTracker positionTracker) {
        Set<Position> coveredPositions = positionTracker.getCoveredPositions();

        if (!Room.overlap(x - 1, y + 1, coveredPositions)) {

            if (x - 1 >= 0 && y + 1 < getYDimension()) {
                world[x - 1][y + 1] = Tileset.WALL;
                coveredPositions.add(new Position(x - 1, y + 1));
            }
        }


        if (!Room.overlap(x + 1, y + 1, coveredPositions)) {
            if (x + 1 < getXDimension() && y + 1 < getYDimension()) {
                world[x + 1][y + 1] = Tileset.WALL;
                coveredPositions.add(new Position(x + 1, y + 1));
            }
        }


        if (!Room.overlap(x - 1, y - 1, coveredPositions)) {
            if (x - 1 >= 0 && y - 1 >= 0) {
                world[x - 1][y - 1] = Tileset.WALL;
                coveredPositions.add(new Position(x - 1, y - 1));
            }
        }


        if (!Room.overlap(x + 1, y - 1, coveredPositions)) {
            if (x + 1 < getXDimension() && y - 1 >= 0) {
                world[x + 1][y - 1] = Tileset.WALL;
                coveredPositions.add(new Position(x + 1, y - 1));
            }
        }

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
}

