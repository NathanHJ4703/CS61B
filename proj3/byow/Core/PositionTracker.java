package byow.Core;

import java.util.*;

public class PositionTracker {
    private List<Position> coveredWallPositions;
    private  List<Position> coveredPositions;
    private Map<Position, Room> wallToRoom;
    private Position avatar;

    public PositionTracker() {
        coveredWallPositions = new LinkedList<>();
        coveredPositions = new LinkedList<>();
        wallToRoom = new HashMap<>();
    }

    public void addWallToRoom(Position p, Room room) {
        wallToRoom.put(p, room);
    }

    public void removeWallToRoom(Position p) {
        wallToRoom.remove(p);
    }

    public void removeCoveredWallPositions(Position p) {
        coveredWallPositions.remove(p);
    }

    public void addCoveredPositions(Position p) {
        coveredPositions.add(p);
    }

    public List<Position> getCoveredPositions() {
        return coveredPositions;
    }

    public void addCoveredWallPositions(Position p) {
        coveredWallPositions.add(p);
    }

    public List<Position> getCoveredWallPositions() {
        return coveredWallPositions;
    }

    public Map<Position, Room> getWallToRoom() {
        return wallToRoom;
    }
}
