package byow.Core;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class VerticalHallway extends Room {
    private boolean goingUp;

    public VerticalHallway(Position p, int height, boolean goingUp) {
        super(p, 3, height);
        this.goingUp = goingUp;
    }

    public boolean getGoingUp() {
        return goingUp;
    }

    @Override
    public List<Position> getWallCoordinates() {
        List<Position> wallCoordinates = new LinkedList<>();
        downwardWall(wallCoordinates);
        return wallCoordinates;
    }

    private void downwardWall(List<Position> wallCoordinates) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (x == 0 || x == 2 || y == 0) {
                    wallCoordinates.add(new Position(x + getBottomLeft().getX(), y + getBottomLeft().getY()));
                    OurWorld.coveredWallPositions.add(new Position(x + getBottomLeft().getX(), y + getBottomLeft().getY()));
                }
            }
        }
    }

    @Override
    public List<Position> getFloorCoordinates() {
        List<Position> floorCoordinates = new LinkedList<>();
        downwardFloor(floorCoordinates);
        return floorCoordinates;
    }

    private void downwardFloor(List<Position> floorCoordinates) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < getHeight()+1; y++) {
                if (x == 1 && y != 0) {
                    floorCoordinates.add(new Position(x + getBottomLeft().getX(), y + getBottomLeft().getY()));
                    //OurWorld.coveredPositions.add(new Position(x + getBottomLeft().getX(), y + getBottomLeft().getY()));
                }
            }
        }
    }

}
