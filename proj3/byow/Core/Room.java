package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Room {
    private Position bottomLeft;
    private int width;
    private int height;
    private boolean overlap;
    private boolean connected;
    private boolean isTopOpen;
    private boolean isRightOpen;
    private boolean isBottomOpen;
    private boolean isLeftOpen;
    private List<Position> walls;
    private int id;


    public Room(Position position, int width, int height, int id) {
        bottomLeft = position;
        this.width = width;
        this.height = height;
        overlap = false;
        connected = false;
        isTopOpen = true;
        isRightOpen = true;
        isBottomOpen = getBottomLeft().getY() >= 4;
        isLeftOpen = getBottomLeft().getX() >= 4;
        walls = new LinkedList<>();
        this.id = id;
    }

    public int getId() {
        return id;
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


    public List<Position> getWallCoordinates(PositionTracker pTracker) {
        List<Position> wallCoordinates = new LinkedList<>();
        Set<Position> tempCovered = new HashSet<>();

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
                if (overlap(x, y, pTracker.getCoveredPositions())) {
                    overlap = true;
                    break;
                }
                if (x == bottomLeft.getX() || x == (rightSide - 1)
                        || y == bottomLeft.getY() || y == (topSide - 1)) {
                    Position p = new Position(x, y);
                    wallCoordinates.add(p);
                    tempCovered.add(p);
                }
            }
            if (getOverlap()) {
                wallCoordinates = new LinkedList<>();
                break;
            }
        }
        if (!getOverlap()) {
            for (Position p : tempCovered) {
                pTracker.addCoveredPositions(p);
                pTracker.addCoveredWallPositions(p);
                pTracker.addWallToRoom(p, this);
                walls.add(p);
            }
        }
        return wallCoordinates;
    }

    public List<Position> getWalls() {
        return walls;
    }




    // returns a list of all the floor coordinates for the room.
    public List<Position> getFloorCoordinates(PositionTracker pTracker, RoomTracker rTracker) {
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
                if (overlap(x, y, pTracker.getCoveredPositions())
                        && !rTracker.getDistinctRooms().contains(this)) {
                    overlap = true;
                    break;
                }
                if (x != bottomLeft.getX() && x != (rightSide - 1)
                        && y != bottomLeft.getY() && y != (topSide - 1)) {
                    floorCoordinates.add(new Position(x, y));
                    //OurWorld.coveredFloorPositions.add(new Position(x, y));
                    pTracker.addCoveredPositions(new Position(x, y));
                }
            }
            if (overlap) {
                floorCoordinates = new LinkedList<>();
                break;
            }
        }

        return floorCoordinates;
    }

    public static boolean overlap(int x, int y, List<Position> positions) {
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
    public List<Position> getOpenCoordinates(Random random) {
        List<Position> openCoordinates = new LinkedList<>();
        if (isBottomOpen) {
            openBottom(openCoordinates, random);
        }
        if (isTopOpen) {
            openTop(openCoordinates, random);
        }
        if (isLeftOpen) {
            openLeft(openCoordinates, random);
        }
        if (isRightOpen) {
            openRight(openCoordinates, random);
        }
        /**
        if (additionalOpenCoordinates.size() > 0) {
            for(Position p : additionalOpenCoordinates) {
                openCoordinates.add(p);
            }
        }*/
        return openCoordinates;
    }



    //Creates a randomly generated hole in a closed room.
    public Position openHole(Random random) {
        if (isBottomOpen) {
            int xPos = getBottomLeft().getX() + random.nextInt(getWidth() - 2) + 1;
            int yPos = getBottomLeft().getY();
            return new Position(xPos, yPos);
        } else if (isTopOpen) {
            int xPos = getBottomLeft().getX() + random.nextInt(getWidth() - 2) + 1;
            int yPos = getBottomLeft().getY() + getHeight() - 1;
            return new Position(xPos, yPos);
        } else if (isLeftOpen) {
            int xPos = getBottomLeft().getX();
            int yPos = getBottomLeft().getY() + random.nextInt(getHeight() - 2) + 1;
            return new Position(xPos, yPos);
        }
        int xPos = getBottomLeft().getX() + getWidth() - 1;
        int yPos = getBottomLeft().getY() + random.nextInt(getHeight() - 2) + 1;
        return new Position(xPos, yPos);
    }

    private void openBottom(List<Position> openCoordinates, Random random) {

        int x = random.nextInt(2);
        if (x == 0) {
            return;
        }
        int xPos = getBottomLeft().getX() + random.nextInt(getWidth() - 2) + 1;
        int yPos = getBottomLeft().getY();
        openCoordinates.add(new Position(xPos, yPos));
    }

    private void openTop(List<Position> openCoordinates, Random random) {

        int x = random.nextInt(2);
        if (x == 0) {
            return;
        }
        int xPos = getBottomLeft().getX() + random.nextInt(getWidth() - 2) + 1;
        int yPos = getBottomLeft().getY() + getHeight() - 1;
        openCoordinates.add(new Position(xPos, yPos));
    }

    private void openLeft(List<Position> openCoordinates, Random random) {

        int x = random.nextInt(2);
        if (x == 0) {
            return;
        }
        int xPos = getBottomLeft().getX();
        int yPos = getBottomLeft().getY() + random.nextInt(getHeight() - 2) + 1;
        openCoordinates.add(new Position(xPos, yPos));
    }

    private void openRight(List<Position> openCoordinates, Random random) {

        int x = random.nextInt(2);
        if (x == 0) {
            return;
        }
        int xPos = getBottomLeft().getX() + getWidth() - 1;
        int yPos = getBottomLeft().getY() + random.nextInt(getHeight() - 2) + 1;
        openCoordinates.add(new Position(xPos, yPos));
    }

    /**public boolean isConnected() {
        return getOpenCoordinates().size() != 0;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return getWidth() == room.getWidth()
                && getHeight() == room.getHeight()
                && getOverlap() == room.getOverlap()
                && isTopOpen == room.isTopOpen
                && isRightOpen == room.isRightOpen
                && isBottomOpen == room.isBottomOpen
                && isLeftOpen == room.isLeftOpen
                && Objects.equals(getBottomLeft(), room.getBottomLeft());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBottomLeft(), getWidth(), getHeight(),
                getOverlap(), isTopOpen, isRightOpen, isBottomOpen, isLeftOpen);
    }

    public static void main(String[] args) {
        Engine e = new Engine();
        int WIDTH = 70;
        int HEIGHT = 40;
        int TILE_SIZE = 16;
        StdDraw.setCanvasSize(WIDTH*TILE_SIZE, HEIGHT*TILE_SIZE);
        StdDraw.text(0.1,0.2, "Hello World");
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.1, 0.2, "Hello World");
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT, 10, 5);
        StdDraw.setPenColor(Color.WHITE);
        Font hud = new Font("Arial", Font.PLAIN, 16);
        StdDraw.setFont(hud);
        TETile[][] worldGenerated = e.interactWithInputString("n6960S");

        ter.renderFrame(worldGenerated);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(35, 35, "Hello World");
        StdDraw.show();
    }

}
