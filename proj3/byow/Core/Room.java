package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Room {
    private Position bottomLeft;
    private static final Random RANDOM = new Random();
    private int width;
    private int height;
    private boolean overlap;
    private boolean connected;
    private Random random;
    private Map<String, Boolean> openSides = new HashMap<>();
    private boolean noOpenTop;
    private boolean noOpenLeft;
    private boolean noOpenRight;
    private boolean noOpenBot;


    public Room(Position position, int width, int height, Random random) {
        bottomLeft = position;
        this.width = width;
        this.height = height;
        overlap = false;
        connected = false;
        this.random = random;
        openSides.put("isTopOpen", true);
        openSides.put("isRightOpen", true);
        if (getBottomLeft().getY() < 4) {
            openSides.put("isBottomOpen", false);
        } else {
            openSides.put("isBottomOpen", true);
        }
        if (getBottomLeft().getX() < 4) {
            openSides.put("isLeftOpen", false);
        } else {
            openSides.put("isLeftOpen", true);
        }
    }

    //For constructing hallways
    public Room(Position position, int width, int height) {
        bottomLeft = position;
        this.width = width;
        this.height = height;
        overlap = false;
        connected = false;
    }

    // Assume that r2 is to the right of r1.
    public static VerticalHallway connect(Room r1, Room r2) {
        VerticalHallway x = null;
        // If bottom left corner of room1 is placed higher than bottom left corner of room2.
        if (r2.getBottomLeft().getY() < r1.getBottomLeft().getY()) {
            int depthHallway = r1.random.nextInt(OurWorld.getMaxHeight());
            while (r1.getBottomLeft().getY() - depthHallway < 0) {
                depthHallway = r1.random.nextInt(OurWorld.getMaxHeight());
            }
            int yPos = r1.getBottomLeft().getY() - depthHallway;
            int xPos = r1.random.nextInt(r1.width - 2) + r1.getBottomLeft().getX();
            x = new VerticalHallway(new Position(xPos, yPos), depthHallway, false);
        }
        return x;
    }



    public boolean getOverlap() {
        return overlap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Returns a list of all the wall coordinates for the room.
    public List<Position> getWallCoordinates() {
        List<Position> wallCoordinates = new LinkedList<>();
        int topSide = bottomLeft.getY() + height;
        if (topSide > OurWorld.getYDimension()) {
            openSides.put("isTopOpen", false);
        }
        while (topSide > OurWorld.getYDimension()) {
            topSide--;
        }
        int rightSide = bottomLeft.getX() + width;
        if (rightSide > OurWorld.getXDimension()) {
            openSides.put("isRightOpen", false);
        }
        while (rightSide > OurWorld.getXDimension()) {
            rightSide--;
        }
        for (int x = bottomLeft.getX(); x < rightSide; x += 1) {
            for (int y = bottomLeft.getY(); y < topSide; y += 1) {
                if (overlap(x, y, OurWorld.coveredPositions)) {
                    overlap = true;
                    break;
                }
                if (x == bottomLeft.getX() || x == (rightSide - 1) || y == bottomLeft.getY() || y == (topSide - 1)) {
                    wallCoordinates.add(new Position(x, y));
                    OurWorld.coveredPositions.add(new Position(x, y));
                }
            }
            if (overlap) {
                wallCoordinates = new LinkedList<>();
                break;
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
                if (overlap(x, y, OurWorld.coveredPositions) && !OurWorld.distinctRooms.contains(this)) {
                    overlap = true;
                    break;
                }
                if (x != bottomLeft.getX() && x != (rightSide - 1) && y != bottomLeft.getY() && y != (topSide - 1)) {
                    floorCoordinates.add(new Position(x, y));
                    OurWorld.coveredPositions.add(new Position(x, y));
                }
            }
            if (overlap) {
                floorCoordinates = new LinkedList<>();
                break;
            }
        }

        return floorCoordinates;
    }

    public boolean overlap(int x, int y, Set<Position> positions) {
        for (Position p : positions) {
            if (p.getX() == x && p.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public Position getBottomLeft() {
        return bottomLeft;
    }


    public static void main(String[] args) {
        Map<String, Boolean> x = new HashMap<>();
    }

}
