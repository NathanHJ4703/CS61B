package byow.Core;

import java.util.*;

public class PositionTracker {
    private List<Position> coveredWallPositions;
    private  Set<Position> coveredPositions;
    private Map<Position, Room> wallToRoom;

    public PositionTracker() {
        coveredWallPositions = new LinkedList<>();
        coveredPositions = new HashSet<>();
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

    public Set<Position> getCoveredPositions() {
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
