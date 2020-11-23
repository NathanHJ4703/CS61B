package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Room {
    private Position bottomLeft;
    private int width;
    private int height;
    private boolean overlap;
    private boolean connected;
    private Random random;
    private boolean isTopOpen;
    private boolean isRightOpen;
    private boolean isBottomOpen;
    private boolean isLeftOpen;


    public Room(Position position, int width, int height, Random random) {
        bottomLeft = position;
        this.width = width;
        this.height = height;
        overlap = false;
        connected = false;
        this.random = random;
        isTopOpen = true;
        isRightOpen = true;
        isBottomOpen = getBottomLeft().getY() >= 4;
        isLeftOpen = getBottomLeft().getX() >= 4;
    }

    //For constructing hallways
    public Room(Position position, int width, int height) {
        bottomLeft = position;
        this.width = width;
        this.height = height;
        overlap = false;
        connected = false;
    }

/**
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
*/


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

        int topSide = bottomLeft.getY() + getHeight();
        int rightSide = bottomLeft.getX() + getWidth();
        if (topSide > OurWorld.getYDimension() - 4) {
            isTopOpen = false;
        }
        while (topSide > OurWorld.getYDimension()) {
            height--;
            topSide--;
        }
        if (rightSide > OurWorld.getXDimension() - 4) {
            isRightOpen = false;
        }
        while (rightSide > OurWorld.getXDimension()) {
            width--;
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
                    checkSurroundings(x, y, topSide, rightSide);
                }
            }
            if (overlap) {
                wallCoordinates = new LinkedList<>();
                break;
            }
        }

        return wallCoordinates;
    }

    private void checkSurroundings(int x, int y, int topSide, int rightSide) {
        if (x == bottomLeft.getX() && y != bottomLeft.getY() && y != topSide - 1) {
            checkLeft(x, y);
        } else if (x == rightSide - 1 && y != bottomLeft.getY() && y != topSide - 1) {
            checkRight(x, y);
        } else if (y == bottomLeft.getY() && x != bottomLeft.getX() && x != rightSide - 1) {
            checkBottom(x, y);
        } else if (y == topSide - 1 && x != bottomLeft.getX() && x != rightSide - 1) {
            checkTop(x, y);
        }
    }

    private void checkBottom(int x, int y) {
        if (overlap(x, y - 1, OurWorld.coveredPositions)) {
            isBottomOpen = false;
        }
    }

    private void checkTop(int x, int y) {
        if (overlap(x, y + 1, OurWorld.coveredPositions)) {
            isTopOpen = false;
        }
    }

    private void checkRight(int x, int y) {
        if (overlap(x + 1, y, OurWorld.coveredPositions)) {
            isRightOpen = false;
        }
    }

    private void checkLeft(int x, int y) {
        if (overlap(x - 1, y, OurWorld.coveredPositions)) {
            isLeftOpen = false;
        }
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

    // Should be called after calling getWallCoordinates.
    public List<Position> getOpenCoordinates() {
        List<Position> openCoordinates = new LinkedList<>();
        if (isBottomOpen) {
            openBottom(openCoordinates);
        }
        if (isTopOpen) {
            openTop(openCoordinates);
        }
        if (isLeftOpen) {
            openLeft(openCoordinates);
        }
        if (isRightOpen) {
            openRight(openCoordinates);
        }
        return openCoordinates;
    }

    private void openBottom(List<Position> openCoordinates) {
        int x = random.nextInt(2);
        if (x == 0) {
            return;
        }
        int xPos = getBottomLeft().getX() + random.nextInt(getWidth() - 2) + 1;
        int yPos = getBottomLeft().getY();
        openCoordinates.add(new Position(xPos, yPos));
    }

    private void openTop(List<Position> openCoordinates) {
        int x = random.nextInt(2);
        if (x == 0) {
            return;
        }
        int xPos = getBottomLeft().getX() + random.nextInt(getWidth()-2) + 1;
        int yPos = getBottomLeft().getY() + getHeight() - 1;
        openCoordinates.add(new Position(xPos, yPos));
    }

    private void openLeft(List<Position> openCoordinates) {
        int x = random.nextInt(2);
        if (x == 0) {
            return;
        }
        int xPos = getBottomLeft().getX();
        int yPos = getBottomLeft().getY() + random.nextInt(getHeight() - 2) + 1;
        openCoordinates.add(new Position(xPos, yPos));
    }

    private void openRight(List<Position> openCoordinates) {
        int x = random.nextInt(2);
        if (x == 0) {
            return;
        }
        int xPos = getBottomLeft().getX() + getWidth() - 1;
        int yPos = getBottomLeft().getY() + random.nextInt(getHeight() - 2) + 1;
        openCoordinates.add(new Position(xPos, yPos));
    }


    public static void main(String[] args) {
        Room r = new Room(new Position(2, 2), 4, 5, new Random(123861));
        System.out.println(r.random.nextInt(2));

    }

}
